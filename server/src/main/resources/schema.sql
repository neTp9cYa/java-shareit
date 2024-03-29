CREATE TABLE IF NOT EXISTS USERS
(
    ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    NAME VARCHAR NULL,
    EMAIL VARCHAR NOT NULL,
    CONSTRAINT USERS_PK PRIMARY KEY(ID),
    CONSTRAINT USERS_EMAIL_UN UNIQUE(EMAIL)
);

CREATE TABLE IF NOT EXISTS ITEM_REQUESTS
(
    ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    USER_ID BIGINT NOT NULL,
    DESCRIPTION VARCHAR NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT ITEM_REQUESTS_PK PRIMARY KEY (ID),
    CONSTRAINT ITEM_REQUESTS_USERS_FK FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    USER_ID BIGINT NOT NULL,
    ITEM_REQUEST_ID BIGINT,
    NAME VARCHAR NOT NULL,
    DESCRIPTION VARCHAR NOT NULL,
    AVAILABLE BOOLEAN NOT NULL,
    CONSTRAINT ITEMS_PK PRIMARY KEY (ID),
    CONSTRAINT ITEMS_USERS_FK FOREIGN KEY (USER_ID) REFERENCES USERS(ID) ON DELETE CASCADE,
    CONSTRAINT ITEMS_ITEM_REQUESTS_FK FOREIGN KEY (ITEM_REQUEST_ID) REFERENCES ITEM_REQUESTS(ID) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS COMMENTS
(
    ID BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    USER_ID BIGINT NOT NULL,
    ITEM_ID BIGINT NOT NULL,
    TEXT VARCHAR NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT COMMENTS_PK PRIMARY KEY (ID),
    CONSTRAINT COMMENTS_USERS_FK FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    CONSTRAINT COMMENTS_ITEMS_FK FOREIGN KEY (ITEM_ID) REFERENCES ITEMS (ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS BOOKINGS
(
    ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
    USER_ID BIGINT NOT NULL,
    ITEM_ID BIGINT NOT NULL,
    START_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    END_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    STATUS INTEGER NOT NULL,
    CONSTRAINT BOOKINGS_PK PRIMARY KEY (ID),
    CONSTRAINT BOOKINGS_USERS_FK FOREIGN KEY (USER_ID) REFERENCES USERS (ID) ON DELETE CASCADE,
    CONSTRAINT BOOKINGS_ITEMS_FK FOREIGN KEY (ITEM_ID) REFERENCES ITEMS (ID) ON DELETE CASCADE
);
