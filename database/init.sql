CREATE TABLE Privacy (
    PrivacyId INT PRIMARY KEY,
    PrivacyName NVARCHAR(50) NOT NULL UNIQUE,
    PrivacyDescription NVARCHAR(100)
);

CREATE TABLE FriendRequestStatus (
    StatusId INT PRIMARY KEY,
    StatusName NVARCHAR(50) NOT NULL UNIQUE,
    StatusDescription NVARCHAR(100)
);

CREATE TABLE ReactionType (
    ReactionTypeId INT PRIMARY KEY,
    TypeName NVARCHAR(50) NOT NULL UNIQUE,
    IconUrl NVARCHAR(255)
);

CREATE TABLE MediaType (
    MediaTypeId INT PRIMARY KEY,
    TypeName NVARCHAR(50) NOT NULL UNIQUE,
    TypeDescription NVARCHAR(100)
);

CREATE TABLE Gender (
    GenderId INT PRIMARY KEY,
    GenderName NVARCHAR(50) NOT NULL UNIQUE,
    GenderDescription NVARCHAR(100)
);

CREATE TABLE Role (
    RoleId INT PRIMARY KEY,
    RoleName NVARCHAR(50) NOT NULL UNIQUE,
    RoleDescription NVARCHAR(100)
);

-- ========= BẢNG CHÍNH (CORE TABLES) =========

CREATE TABLE Users (
    userId BIGINT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(255) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL, -- Mật khẩu phải được băm (hashed)
    firstname NVARCHAR(50) NOT NULL,
    lastname NVARCHAR(50) NOT NULL,
    birthdate DATE,
    genderId INT,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    isOnline BIT NOT NULL DEFAULT 0,

    CONSTRAINT FK_User_Gender FOREIGN KEY (genderId) REFERENCES Gender(genderId)
);

CREATE TABLE FriendRequest (
    friendRequestId BIGINT IDENTITY(1,1) PRIMARY KEY,
    senderId BIGINT NOT NULL,
    receiverId BIGINT NOT NULL,
    statusId INT NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_FriendRequest_Sender FOREIGN KEY (senderId) REFERENCES Users(userId),
    CONSTRAINT FK_FriendRequest_Receiver FOREIGN KEY (receiverId) REFERENCES Users(userId),
    CONSTRAINT FK_FriendRequest_Status FOREIGN KEY (statusId) REFERENCES FriendRequestStatus(statusId),
    CONSTRAINT UQ_FriendRequest_Pair UNIQUE (senderId, receiverId) -- Ngăn chặn gửi nhiều request trùng lặp
);

CREATE TABLE Post (
    postId BIGINT IDENTITY(1,1) PRIMARY KEY,
    authorId BIGINT NOT NULL,
    content NVARCHAR(MAX),
    originalpostId BIGINT NULL, -- Cho chức năng chia sẻ (Share)
    privacyId INT NOT NULL,
    sharecount INT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2,

    CONSTRAINT FK_Post_Author FOREIGN KEY (authorId) REFERENCES Users(userId) ON DELETE CASCADE,
    CONSTRAINT FK_Post_OriginalPost FOREIGN KEY (originalpostId) REFERENCES Post(postId), -- Self-referencing
    CONSTRAINT FK_Post_Privacy FOREIGN KEY (privacyId) REFERENCES Privacy(privacyId)
);

CREATE TABLE Conversation (
    conversationId BIGINT IDENTITY(1,1) PRIMARY KEY,
    conversationname NVARCHAR(100), -- Cho group chat
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);

CREATE TABLE Notification (
    notificationId BIGINT IDENTITY(1,1) PRIMARY KEY,
    recipientId BIGINT NOT NULL,
    actorId BIGINT NOT NULL,
    content NVARCHAR(255) NOT NULL,
    isRead BIT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    -- Thêm các cột để trỏ đến nguồn của notification
    postId BIGINT NULL,
    commentId BIGINT NULL,

    CONSTRAINT FK_Notification_Recipient FOREIGN KEY (recipientId) REFERENCES Users(userId) ON DELETE CASCADE,
    CONSTRAINT FK_Notification_Actor FOREIGN KEY (actorId) REFERENCES Users(userId), -- No cascade on actor
    CONSTRAINT FK_Notification_Post FOREIGN KEY (postId) REFERENCES Post(postId)
    -- FK to Comment will be added after Comment table is created
);


-- ========= BẢNG PHỤ THUỘC & BẢNG TRUNG GIAN =========

CREATE TABLE Comment (
    commentId BIGINT IDENTITY(1,1) PRIMARY KEY,
    authorId BIGINT NOT NULL,
    postId BIGINT NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2,

    CONSTRAINT FK_Comment_Author FOREIGN KEY (authorId) REFERENCES Users(userId),
    CONSTRAINT FK_Comment_Post FOREIGN KEY (postId) REFERENCES Post(postId) ON DELETE CASCADE
);

-- Thêm FK còn thiếu vào bảng Notification
ALTER TABLE Notification
ADD CONSTRAINT FK_Notification_Comment FOREIGN KEY (commentId) REFERENCES Comment(commentId);

CREATE TABLE Reaction (
    reactionId BIGINT IDENTITY(1,1) PRIMARY KEY,
    authorId BIGINT NOT NULL,
    reactionTypeId INT NOT NULL,
    postId BIGINT NULL,
    commentId BIGINT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),

    -- Các khóa ngoại
    CONSTRAINT FK_Reaction_Author FOREIGN KEY (authorId) REFERENCES Users(userId),
    CONSTRAINT FK_Reaction_Type FOREIGN KEY (reactionTypeId) REFERENCES ReactionType(reactionTypeId),
    CONSTRAINT FK_Reaction_Post FOREIGN KEY (postId) REFERENCES Post(postId) ON DELETE CASCADE,
    CONSTRAINT FK_Reaction_Comment FOREIGN KEY (commentId) REFERENCES Comment(commentId), -- Sửa 'Comment(commentId)' thành tên cột PK của bảng Comment

    -- CHECK constraint này có thể giữ lại bên trong
    CONSTRAINT CHK_Reaction_Target CHECK (
        (postId IS NOT NULL AND commentId IS NULL) OR (postId IS NULL AND commentId IS NOT NULL)
    ) -- Đảm bảo reaction chỉ thuộc về Post hoặc Comment
);

-- User chỉ có 1 reaction trên 1 post
CREATE UNIQUE INDEX UQ_Reaction_Post
ON Reaction(authorId, postId)
WHERE commentId IS NULL;


-- User chỉ có 1 reaction trên 1 comment
CREATE UNIQUE INDEX UQ_Reaction_Comment
ON Reaction(authorId, commentId)
WHERE postId IS NULL;


CREATE TABLE Media (
    mediaId BIGINT IDENTITY(1,1) PRIMARY KEY,
    mediaUrl NVARCHAR(500) NOT NULL,
    mediatypeId INT NOT NULL,
    uploadorder INT NOT NULL DEFAULT 0,
    postId BIGINT NULL,
    commentId BIGINT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Media_Type FOREIGN KEY (mediatypeId) REFERENCES MediaType(mediatypeId),
    CONSTRAINT FK_Media_Post FOREIGN KEY (postId) REFERENCES Post(postId) ON DELETE CASCADE,
    CONSTRAINT FK_Media_Comment FOREIGN KEY (commentId) REFERENCES Comment(commentId),
    CONSTRAINT CHK_Media_Target CHECK (
        (postId IS NOT NULL AND commentId IS NULL) OR (postId IS NULL AND commentId IS NOT NULL)
    )
);

CREATE TABLE Message (
    messageId BIGINT IDENTITY(1,1) PRIMARY KEY,
    conversationId BIGINT NOT NULL,
    senderId BIGINT NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Message_Conversation FOREIGN KEY (conversationId) REFERENCES Conversation(conversationId) ON DELETE CASCADE,
    CONSTRAINT FK_Message_Sender FOREIGN KEY (senderId) REFERENCES Users(userId)
);

CREATE TABLE UserRole (
    userId BIGINT NOT NULL,
    roleId INT NOT NULL,
    PRIMARY KEY (userId, roleId),
    CONSTRAINT FK_UserRole_User FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE,
    CONSTRAINT FK_UserRole_Role FOREIGN KEY (roleId) REFERENCES Role(roleId) ON DELETE CASCADE
);

CREATE TABLE ConversationParticipant (
    conversationId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    PRIMARY KEY (conversationId, userId),
    CONSTRAINT FK_Participant_Conversation FOREIGN KEY (conversationId) REFERENCES Conversation(conversationId) ON DELETE CASCADE,
    CONSTRAINT FK_Participant_User FOREIGN KEY (userId) REFERENCES Users(userId) ON DELETE CASCADE
);


-- ========= TẠO INDEXES ĐỂ TĂNG HIỆU NĂNG =========
GO
CREATE INDEX IX_User_Email ON Users(email);
CREATE INDEX IX_Post_AuthorId ON Post(authorId);
CREATE INDEX IX_Comment_PostId ON Comment(postId);
CREATE INDEX IX_Reaction_PostId ON Reaction(postId);
CREATE INDEX IX_Reaction_CommentId ON Reaction(commentId);
CREATE INDEX IX_FriendRequest_ReceiverId ON FriendRequest(receiverId);
CREATE INDEX IX_Message_ConversationId ON Message(conversationId);
CREATE INDEX IX_Notification_RecipientId ON Notification(recipientId);
GO


-- ========= CHÈN DỮ LIỆU BAN ĐẦU (SEED DATA) CHO CÁC BẢNG THAM CHIẾU =========
GO
INSERT INTO Privacy (privacyId, privacyName) VALUES
(1, 'Public'), (2, 'Friend'), (3, 'Private');

INSERT INTO FriendRequestStatus (statusId, statusName) VALUES
(1, 'Pending'), (2, 'Accepted'), (3, 'Rejected');

INSERT INTO ReactionType (reactionTypeId, typeName) VALUES
(1, 'Like'), (2, 'Love'), (3, 'Haha'), (4, 'Wow'), (5, 'Sad'), (6, 'Angry');

INSERT INTO MediaType (mediaTypeId, typeName) VALUES
(1, 'Image'), (2, 'Video');

INSERT INTO Gender (genderId, genderName) VALUES
(1, 'Male'), (2, 'Female'), (3, 'Other');

INSERT INTO Role (roleId, roleName) VALUES
(1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

GO
PRINT 'Database schema created successfully!';
