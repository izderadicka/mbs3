INSERT INTO "genre" ("id", "version_id", "name")
VALUES (1, 1, 'Adventure'),
    (2, 1, 'Biography'),
    (3, 1, 'Business/Investing'),
    (4, 1, 'Children''s Books'),
    (5, 1, 'Computers/Internet'),
    (6, 1, 'Cooking/Food'),
    (7, 1, 'Crime/Mystery'),
    (8, 1, 'Essay'),
    (9, 1, 'Fantasy'),
    (10, 1, 'Fiction'),
    (11, 1, 'Health'),
    (12, 1, 'History'),
    (13, 1, 'Horror'),
    (14, 1, 'Humor/Satire'),
    (15, 1, 'Non-Fiction'),
    (16, 1, 'Novels'),
    (17, 1, 'Philosophy'),
    (18, 1, 'Plays'),
    (19, 1, 'Poetry'),
    (20, 1, 'Politics'),
    (21, 1, 'Psychology'),
    (22, 1, 'Religion'),
    (23, 1, 'Romance'),
    (24, 1, 'Science'),
    (25, 1, 'Science Fiction'),
    (26, 1, 'Sexuality'),
    (27, 1, 'Short Fiction'),
    (28, 1, 'Sports'),
    (29, 1, 'Thriller'),
    (30, 1, 'Travel'),
    (31, 1, 'War'),
    (32, 1, 'Western'),
    (34, 1, 'Educational'),
    (35, 1, 'Social Fiction'),
    (36, 1, 'Autobiography'),
    (37, 1, 'Youth''s Books'),
    (38, 1, 'Historical Fiction'),
    (39, 1, 'Military'),
    (40, 1, 'Hobby'),
    (41, 1, 'Mysteries'),
    (42, 1, 'Social Sciences'),
    (43, 1, 'Espionage'),
    (44, 1, 'Post-catastrophic'),
    (45, 1, 'Women''s Books'),
    (46, 1, 'Nature'),
    (47, 1, 'Fairy Tales And Myths'),
    (48, 1, 'Military SF'),
    (49, 1, 'Urban fantasy'),
    (50, 1, 'Comics'),
    (51, 1, 'Space Opera'),
    (52, 1, 'Magazine'),
    (53, 1, 'Action'),
    (54, 1, 'Psychological Fiction'),
    (55, 1, 'Art'),
    (56, 1, 'Mystic'),
    (57, 1, 'Alternative'),
    (58, 1, 'Technology');
ALTER SEQUENCE genre_seq RESTART WITH 59;
INSERT INTO "language" ("id", "version_id", "code", "name")
VALUES (1, 1, 'cs', 'Czech'),
    (2, 1, 'en', 'English'),
    (3, 1, 'sk', 'Slovak'),
    (4, 1, 'ru', 'Russian');
ALTER SEQUENCE language_seq RESTART WITH 5;
INSERT INTO "author" (
        "id",
        "version_id",
        "created",
        "modified",
        "last_name",
        "first_name",
        "description",
        "created_by",
        "modified_by"
    )
VALUES (
        1,
        1,
        '2014-06-08 12:19:19',
        '2014-06-08 12:19:19',
        'Iggulden',
        'Conn',
        NULL,
        '1',
        '1'
    ),
    (
        2,
        1,
        '2010-07-25 11:33:53',
        '2010-08-11 11:56:08',
        'Adams',
        'Douglas',
        NULL,
        '1',
        '1'
    ),
    (
        3,
        1,
        '2010-07-25 11:34:05',
        '2010-07-25 11:34:05',
        'Adamski',
        'George',
        NULL,
        '1',
        '1'
    ),
    (
        4,
        1,
        '2010-07-25 11:34:05',
        '2010-07-25 11:34:05',
        'Adrejev',
        'Leonid',
        NULL,
        '1',
        '1'
    );
SELECT setval(
        'author_seq',
        COALESCE(
            (
                SELECT MAX(id) + 1
                FROM author
            ),
            1
        ),
        false
    );