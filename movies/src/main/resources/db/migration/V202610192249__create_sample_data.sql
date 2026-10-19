BEGIN;

INSERT INTO person(id, first_name, last_name, title, created_at) VALUES
(1, 'Robert', 'Downey Jr.', 'Actor', CURRENT_TIMESTAMP),
(2, 'Gwyneth', 'Paltrow', 'Actor', CURRENT_TIMESTAMP),
(3, 'Terrence', 'Howard', 'Actor', CURRENT_TIMESTAMP),
(4, 'Jon', 'Favreau', 'Director', CURRENT_TIMESTAMP),
(5, 'Mark', 'Fergus', 'Writer', CURRENT_TIMESTAMP),
(6, 'Hawk', 'Ostby', 'Writer', CURRENT_TIMESTAMP),
(7, 'Art', 'Marcum', 'Writer', CURRENT_TIMESTAMP);

INSERT INTO movie(id, title, summary, synopsis, release_year, rating, created_at) VALUES
(1, 'Iron Man',
 'After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.',
 'After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.',
 2008, 7.9, CURRENT_TIMESTAMP
),
(2, 'Captain America: The First Avenger',
 'Steve Rogers, a rejected military soldier, transforms into Captain America after taking a dose of a "Super-Soldier serum". But being Captain America comes at a price as he attempts to take down a warmonger and a terrorist organization.',
 'Steve Rogers, a rejected military soldier, transforms into Captain America after taking a dose of a "Super-Soldier serum". But being Captain America comes at a price as he attempts to take down a warmonger and a terrorist organization.',
 2011, 6.9, CURRENT_TIMESTAMP
);

INSERT INTO job (id, movie_id, person_id, sequence, job_type) VALUES
(1, 1, 1, 1, 'CAST'),
(2, 1, 2, 2, 'CAST'),
(3, 1, 3, 3, 'CAST'),
(4, 1, 4, 1, 'CREW'),
(5, 1, 5, 2, 'CREW'),
(6, 1, 6, 2, 'CREW'),
(7, 1, 7, 2, 'CREW');

INSERT INTO job_cast(job_id, character_name) VALUES
(1, 'Tony Stark'),
(2, 'Pepper Potts'),
(3, 'James Rhodes');

INSERT INTO job_crew(job_id, job_name) VALUES
(4, 'Director'),
(5, 'Writer'),
(6, 'Writer'),
(7, 'Writer');

COMMIT;

DO $$
BEGIN
PERFORM setval(
               pg_get_serial_sequence('movie', 'id'),
               COALESCE((SELECT MAX(id) FROM movie), 1),
               (SELECT COUNT(*) > 0 FROM movie)
       );
PERFORM setval(
               pg_get_serial_sequence('person', 'id'),
               COALESCE((SELECT MAX(id) FROM person), 1),
               (SELECT COUNT(*) > 0 FROM person)
       );
PERFORM setval(
               pg_get_serial_sequence('job', 'id'),
               COALESCE((SELECT MAX(id) FROM job), 1),
               (SELECT COUNT(*) > 0 FROM job)
       );

END $$;