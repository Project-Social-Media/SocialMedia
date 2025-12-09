/* * NEW DATABASE */
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'SocialNetworkDB')
BEGIN
    CREATE DATABASE SocialNetworkDB;
    PRINT 'Database SocialNetworkDB đã được tạo.';
END
ELSE
BEGIN
    PRINT 'Database SocialNetworkDB đã tồn tại.';
END
GO

USE SocialNetworkDB;
GO

/* LOOKUP TABLES */

CREATE TABLE privacy (
    privacyId INT PRIMARY KEY,
    privacyName NVARCHAR(50) NOT NULL UNIQUE,
    privacyDescription NVARCHAR(100)
);

CREATE TABLE friendRequestStatus (
    statusId INT PRIMARY KEY,
    statusName NVARCHAR(50) NOT NULL UNIQUE,
    statusDescription NVARCHAR(100)
);

CREATE TABLE reactionType (
    reactionTypeId INT PRIMARY KEY,
    typeName NVARCHAR(50) NOT NULL UNIQUE,
    iconUrl NVARCHAR(255)
);

CREATE TABLE mediaType (
    mediaTypeId INT PRIMARY KEY,
    typeName NVARCHAR(50) NOT NULL UNIQUE,
    typeDescription NVARCHAR(100)
);

CREATE TABLE gender (
    genderId INT PRIMARY KEY,
    genderName NVARCHAR(50) NOT NULL UNIQUE,
    genderDescription NVARCHAR(100)
);

CREATE TABLE roles (
    roleId INT PRIMARY KEY,
    roleName NVARCHAR(50) NOT NULL UNIQUE,
    roleDescription NVARCHAR(100)
);

-- ========= BẢNG CHÍNH (CORE TABLES) =========

CREATE TABLE users (
    userId BIGINT IDENTITY(1,1) PRIMARY KEY,
    email NVARCHAR(255) NOT NULL UNIQUE,
    password NVARCHAR(255) NOT NULL, -- Mật khẩu phải được băm (hashed)
    firstname NVARCHAR(50) NOT NULL,
    lastname NVARCHAR(50) NOT NULL,
    birthdate DATE,
    genderId INT,
    profilePictureId BIGINT NULL,
    backgroundPictureId BIGINT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    isOnline BIT NOT NULL DEFAULT 0,

    CONSTRAINT FK_User_Gender FOREIGN KEY (genderId) REFERENCES gender(genderId),
--    CONSTRAINT FK_User_ProfilePicture FOREIGN KEY (profilePictureId) REFERENCES media(mediaId),
--    CONSTRAINT FK_User_BackgroundPicture FOREIGN KEY (backgroundPictureId) REFERENCES media(mediaId)

);

CREATE TABLE friendRequest (
    friendRequestId BIGINT IDENTITY(1,1) PRIMARY KEY,
    senderId BIGINT NOT NULL,
    receiverId BIGINT NOT NULL,
    statusId INT NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_FriendRequest_Sender FOREIGN KEY (senderId) REFERENCES users(userId),
    CONSTRAINT FK_FriendRequest_Receiver FOREIGN KEY (receiverId) REFERENCES users(userId),
    CONSTRAINT FK_FriendRequest_Status FOREIGN KEY (statusId) REFERENCES friendRequestStatus(statusId),
    CONSTRAINT UQ_FriendRequest_Pair UNIQUE (senderId, receiverId) -- Ngăn chặn gửi nhiều request trùng lặp
);

CREATE TABLE post (
    postId BIGINT IDENTITY(1,1) PRIMARY KEY,
    authorId BIGINT NOT NULL,
    content NVARCHAR(MAX),
    originalpostId BIGINT NULL, -- Cho chức năng chia sẻ (Share)
    privacyId INT NOT NULL,
    sharecount INT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2,

    CONSTRAINT FK_Post_Author FOREIGN KEY (authorId) REFERENCES users(userId) ON DELETE CASCADE,
    CONSTRAINT FK_Post_OriginalPost FOREIGN KEY (originalpostId) REFERENCES post(postId), -- Self-referencing
    CONSTRAINT FK_Post_Privacy FOREIGN KEY (privacyId) REFERENCES privacy(privacyId)
);

CREATE TABLE conversation (
    conversationId BIGINT IDENTITY(1,1) PRIMARY KEY,
    conversationname NVARCHAR(100), -- Cho group chat
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE()
);

CREATE TABLE notification (
    notificationId BIGINT IDENTITY(1,1) PRIMARY KEY,
    recipientId BIGINT NOT NULL,
    actorId BIGINT NOT NULL,
    content NVARCHAR(255) NOT NULL,
    isRead BIT NOT NULL DEFAULT 0,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    -- Thêm các cột để trỏ đến nguồn của notification
    postId BIGINT NULL,
    commentId BIGINT NULL,

    CONSTRAINT FK_Notification_Recipient FOREIGN KEY (recipientId) REFERENCES users(userId) ON DELETE CASCADE,
    CONSTRAINT FK_Notification_Actor FOREIGN KEY (actorId) REFERENCES users(userId), -- No cascade on actor
    CONSTRAINT FK_Notification_Post FOREIGN KEY (postId) REFERENCES post(postId)
    -- FK to Comment will be added after Comment table is created
);


-- ========= BẢNG PHỤ THUỘC & BẢNG TRUNG GIAN =========

CREATE TABLE comment (
    commentId BIGINT IDENTITY(1,1) PRIMARY KEY,
    authorId BIGINT NOT NULL,
    postId BIGINT NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),
    updatedAt DATETIME2,

    CONSTRAINT FK_Comment_Author FOREIGN KEY (authorId) REFERENCES users(userId),
    CONSTRAINT FK_Comment_Post FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE CASCADE
);

-- Thêm FK còn thiếu vào bảng Notification
ALTER TABLE notification
ADD CONSTRAINT FK_Notification_Comment FOREIGN KEY (commentId) REFERENCES comment(commentId);

CREATE TABLE reaction (
    reactionId BIGINT IDENTITY(1,1) PRIMARY KEY,
    authorId BIGINT NOT NULL,
    reactionTypeId INT NOT NULL,
    postId BIGINT NULL,
    commentId BIGINT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),

    -- Các khóa ngoại
    CONSTRAINT FK_Reaction_Author FOREIGN KEY (authorId) REFERENCES users(userId),
    CONSTRAINT FK_Reaction_Type FOREIGN KEY (reactionTypeId) REFERENCES reactionType(reactionTypeId),
    CONSTRAINT FK_Reaction_Post FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE CASCADE,
    CONSTRAINT FK_Reaction_Comment FOREIGN KEY (commentId) REFERENCES comment(commentId), -- Sửa 'Comment(commentId)' thành tên cột PK của bảng Comment

    -- CHECK constraint này có thể giữ lại bên trong
    CONSTRAINT CHK_Reaction_Target CHECK (
        (postId IS NOT NULL AND commentId IS NULL) OR (postId IS NULL AND commentId IS NOT NULL)
    ) -- Đảm bảo reaction chỉ thuộc về Post hoặc Comment
);

SET QUOTED_IDENTIFIER ON;
GO

-- User chỉ có 1 reaction trên 1 post
CREATE UNIQUE INDEX UQ_Reaction_Post
ON reaction(authorId, postId)
WHERE commentId IS NULL;


-- User chỉ có 1 reaction trên 1 comment
CREATE UNIQUE INDEX UQ_Reaction_Comment
ON reaction(authorId, commentId)
WHERE postId IS NULL;


CREATE TABLE media (
    mediaId BIGINT IDENTITY(1,1) PRIMARY KEY,
    mediaUrl NVARCHAR(500) NOT NULL,
    mediaSize INT NOT NULL,
    mediatypeId INT NOT NULL,
    uploadorder INT NOT NULL DEFAULT 0,
    cloudId NVARCHAR(255) NOT NULL,
    userId BIGINT NOT NULL,
    postId BIGINT NULL,
    commentId BIGINT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Media_Type FOREIGN KEY (mediatypeId) REFERENCES mediaType(mediaTypeId),
    CONSTRAINT FK_Media_User FOREIGN KEY (userId) REFERENCES users(userId),
    CONSTRAINT FK_Media_Post FOREIGN KEY (postId) REFERENCES post(postId) ON DELETE CASCADE,
    CONSTRAINT FK_Media_Comment FOREIGN KEY (commentId) REFERENCES  comment(commentId),
    CONSTRAINT CHK_Media_Target CHECK (
        (postId IS NOT NULL AND commentId IS NULL) OR (postId IS NULL AND commentId IS NOT NULL)
    )
);

ALTER TABLE users
ADD
    CONSTRAINT FK_User_ProfilePicture FOREIGN KEY (profilePictureId) REFERENCES media(mediaId),
    CONSTRAINT FK_User_BackgroundPicture FOREIGN KEY (backgroundPictureId) REFERENCES media(mediaId);

CREATE TABLE message (
    messageId BIGINT IDENTITY(1,1) PRIMARY KEY,
    conversationId BIGINT NOT NULL,
    senderId BIGINT NOT NULL,
    content NVARCHAR(MAX) NOT NULL,
    createdAt DATETIME2 NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Message_Conversation FOREIGN KEY (conversationId) REFERENCES conversation(conversationId) ON DELETE CASCADE,
    CONSTRAINT FK_Message_Sender FOREIGN KEY (senderId) REFERENCES users(userId)
);

CREATE TABLE userRole (
    userId BIGINT NOT NULL,
    roleId INT NOT NULL,
    PRIMARY KEY (userId, roleId),
    CONSTRAINT FK_userRole_User FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE,
    CONSTRAINT FK_userRole_Role FOREIGN KEY (roleId) REFERENCES roles(roleId) ON DELETE CASCADE
);

CREATE TABLE conversationParticipant (
    conversationId BIGINT NOT NULL,
    userId BIGINT NOT NULL,
    PRIMARY KEY (conversationId, userId),
    CONSTRAINT FK_Participant_Conversation FOREIGN KEY (conversationId) REFERENCES conversation(conversationId) ON DELETE CASCADE,
    CONSTRAINT FK_Participant_User FOREIGN KEY (userId) REFERENCES users(userId) ON DELETE CASCADE
);


-- ========= TẠO INDEXES ĐỂ TĂNG HIỆU NĂNG =========
GO
USE SocialNetworkDB;
GO
CREATE INDEX IX_User_Email ON users(email);
CREATE INDEX IX_Post_AuthorId ON post(authorId);
CREATE INDEX IX_Comment_PostId ON comment(postId);
CREATE INDEX IX_Reaction_PostId ON reaction(postId);
CREATE INDEX IX_Reaction_CommentId ON reaction(commentId);
CREATE INDEX IX_FriendRequest_ReceiverId ON friendRequest(receiverId);
CREATE INDEX IX_Message_ConversationId ON message(conversationId);
CREATE INDEX IX_Notification_RecipientId ON notification(recipientId);
GO


-- ========= CHÈN DỮ LIỆU BAN ĐẦU (SEED DATA) CHO CÁC BẢNG THAM CHIẾU =========
GO
INSERT INTO privacy (privacyId, privacyName) VALUES
(1, 'Public'), (2, 'Friend'), (3, 'Private');

INSERT INTO friendRequestStatus (statusId, statusName) VALUES
(1, 'Pending'), (2, 'Accepted'), (3, 'Rejected');

INSERT INTO reactionType (reactionTypeId, typeName) VALUES
(1, 'Like'), (2, 'Love'), (3, 'Haha'), (4, 'Wow'), (5, 'Sad'), (6, 'Angry');

INSERT INTO mediaType (mediaTypeId, typeName) VALUES
(1, 'Image'), (2, 'Video');

INSERT INTO gender (genderId, genderName) VALUES
(1, 'Male'), (2, 'Female'), (3, 'Other');

INSERT INTO roles (roleId, roleName) VALUES
(1, 'USER'), (2, 'ADMIN');

GO
PRINT 'Database schema created successfully!';
