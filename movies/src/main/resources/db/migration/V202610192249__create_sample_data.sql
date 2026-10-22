BEGIN;

ALTER TABLE person ADD COLUMN code VARCHAR(50);
ALTER TABLE movie ADD COLUMN code VARCHAR(50);
ALTER TABLE job ADD COLUMN code VARCHAR(50);

INSERT INTO movie( title, summary, synopsis, release_year, release_date, rating, created_at, code) VALUES
('Iron Man',
 'After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.',
 'After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.',
 2008, '2008-05-02', 7.9, CURRENT_TIMESTAMP, 'mcu-im'
),
('The Incredible Hulk',
 'Dr Bruce Banner subjects himself to high levels of gamma radiation, which triggers his transformation into a huge green creature, the Hulk, whenever he experiences negative emotions such as anger.',
 'Dr Bruce Banner subjects himself to high levels of gamma radiation, which triggers his transformation into a huge green creature, the Hulk, whenever he experiences negative emotions such as anger.',
 2008, '2008-06-13', 6.6, CURRENT_TIMESTAMP, 'mcu-tih'
),
('Iron Man 2',
 'Tony Stark is under pressure from various sources, including the government, to share his technology with the world. He must find a way to fight them while also tackling his other enemies.',
 'Tony Stark is under pressure from various sources, including the government, to share his technology with the world. He must find a way to fight them while also tackling his other enemies.',
 2010, '2010-05-07', 6.9, CURRENT_TIMESTAMP, 'mcu-i-m-2'
),
('Thor',
 'Thor is exiled by his father, Odin, the King of Asgard, to the Earth to live among mortals. When he lands on Earth, his trusted weapon Mjolnir is discovered and captured by S.H.I.E.L.D..',
 'Thor is exiled by his father, Odin, the King of Asgard, to the Earth to live among mortals. When he lands on Earth, his trusted weapon Mjolnir is discovered and captured by S.H.I.E.L.D.',
 2011, '2011-05-06', 7.0, CURRENT_TIMESTAMP, 'mcu-t'
),
('Captain America: The First Avenger',
 'Steve Rogers, a rejected military soldier, transforms into Captain America after taking a dose of a "Super-Soldier serum". But being Captain America comes at a price as he attempts to take down a warmonger and a terrorist organization.',
 'Steve Rogers, a rejected military soldier, transforms into Captain America after taking a dose of a "Super-Soldier serum". But being Captain America comes at a price as he attempts to take down a warmonger and a terrorist organization.',
 2011, '2011-07-22', 6.9, CURRENT_TIMESTAMP, 'mcu-ca-tfa'
),
( 'The Avengers',
 'S.H.I.E.L.D. leader Nick Fury is compelled to launch the Avengers programme when Loki poses a threat to planet Earth. But the superheroes must learn to work together if they are to stop him in time.',
 'S.H.I.E.L.D. leader Nick Fury is compelled to launch the Avengers programme when Loki poses a threat to planet Earth. But the superheroes must learn to work together if they are to stop him in time.',
 2012, '2012-05-04', 8.0, CURRENT_TIMESTAMP, 'mcu-ta'
),
('Iron Man 3',
'Suffering from PTSD, Tony Stark encounters a formidable foe called the Mandarin. When he watches his world fall apart, he must rely on his own instincts as he embarks on a journey of retribution.',
'Suffering from PTSD, Tony Stark encounters a formidable foe called the Mandarin. When he watches his world fall apart, he must rely on his own instincts as he embarks on a journey of retribution.',
2013, '2013-05-03', 7.1, CURRENT_TIMESTAMP, 'mcu-i-m-3'
),
('Thor: The Dark World',
'While investigating a phenomenon, Jane gets sucked into a wormhole. Thor learns about her disappearance and returns to earth just as she reappears. But sensing something amiss, he takes her to Asgard.',
'While investigating a phenomenon, Jane gets sucked into a wormhole. Thor learns about her disappearance and returns to earth just as she reappears. But sensing something amiss, he takes her to Asgard.',
2013, '2013-11-08', 6.7, CURRENT_TIMESTAMP, 'mcu-t-tdw'
),
('Captain America: The Winter Soldier',
'Steve Rogers, along with Black Widow and Falcon, must uncover the secrets hidden within S.H.I.E.L.D., while also battling a dangerous new enemy, who is only known as The Winter Soldier.',
'Steve Rogers, along with Black Widow and Falcon, must uncover the secrets hidden within S.H.I.E.L.D., while also battling a dangerous new enemy, who is only known as The Winter Soldier.',
2014, '2014-04-04', 7.7, CURRENT_TIMESTAMP, 'mcu-ca-tws'
),
('Guardians of the Galaxy',
'A bunch of skilled criminals led by brash adventurer Peter Quill join hands to fight a villain named Ronan the Accuser who wants to control the universe with the help of a mystical orb.',
'A bunch of skilled criminals led by brash adventurer Peter Quill join hands to fight a villain named Ronan the Accuser who wants to control the universe with the help of a mystical orb.',
2014, '2014-08-01', 8.0, CURRENT_TIMESTAMP, 'mcu-gog'
),
('Avengers: Age of Ultron',
'Tony Stark builds an artificial intelligence system named Ultron with the help of Bruce Banner. When the sentient Ultron makes plans to wipe out the human race, the Avengers set out to stop him.',
'Tony Stark builds an artificial intelligence system named Ultron with the help of Bruce Banner. When the sentient Ultron makes plans to wipe out the human race, the Avengers set out to stop him.',
2015, '2015-05-01', 7.3, CURRENT_TIMESTAMP, 'mcu-ca-ta-aou'
),
('Ant-Man',
'Scott, a master thief, gains the ability to shrink in scale with the help of a futuristic suit. Now he must rise to the occasion of his superhero status and protect his secret from unsavoury elements.',
'Scott, a master thief, gains the ability to shrink in scale with the help of a futuristic suit. Now he must rise to the occasion of his superhero status and protect his secret from unsavoury elements.',
2015, '2015-07-17', 7.2, CURRENT_TIMESTAMP, 'mcu-am'
),
('Captain America: Civil War',
'Political involvement in the Avengers'' affairs causes a rift between Captain America and Iron Man.',
'Political involvement in the Avengers'' affairs causes a rift between Captain America and Iron Man.',
2016, '2016-05-06', 7.2, CURRENT_TIMESTAMP, 'mcu-ca-cw'
),
('Doctor Strange',
 'While on a journey of physical and spiritual healing, a brilliant neurosurgeon is drawn into the world of the mystic arts.',
 'While on a journey of physical and spiritual healing, a brilliant neurosurgeon is drawn into the world of the mystic arts.',
 2016, '2016-11-04', 7.2, CURRENT_TIMESTAMP, 'mcu-ds'
),
('Guardians of the Galaxy Vol. 2',
 'The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord''s encounter with his father, the ambitious celestial being Ego.',
 'The Guardians struggle to keep together as a team while dealing with their personal family issues, notably Star-Lord''s encounter with his father, the ambitious celestial being Ego.',
 2017, '2017-05-05', 7.2, CURRENT_TIMESTAMP, 'mcu-gog-v2'
),
('Spider-Man: Homecoming',
 'Peter Parker tries to stop Adrian ''The Vulture'' Toomes from selling weapons made with advanced Chitauri technology while trying to balance his life as an ordinary high school student.',
 'Peter Parker tries to stop Adrian ''The Vulture'' Toomes from selling weapons made with advanced Chitauri technology while trying to balance his life as an ordinary high school student.',
 2017, '2017-07-07', 7.2, CURRENT_TIMESTAMP, 'mcu-sm-hc'
),
('Thor: Ragnarok',
 'Imprisoned on the planet Sakaar, Thor must race against time to return to Asgard and stop Ragnarök, the destruction of his world, at the hands of the powerful and ruthless villain Hela.',
 'Imprisoned on the planet Sakaar, Thor must race against time to return to Asgard and stop Ragnarök, the destruction of his world, at the hands of the powerful and ruthless villain Hela.',
 2017, '2017-11-03', 7.2, CURRENT_TIMESTAMP, 'mcu-t-r'
),
('Black Panther',
 'T''Challa, heir to the hidden but advanced kingdom of Wakanda, must step forward to lead his people into a new future and must confront a challenger from his country''s past.',
 'T''Challa, heir to the hidden but advanced kingdom of Wakanda, must step forward to lead his people into a new future and must confront a challenger from his country''s past.',
 2018, '2018-01-16', 7.2, CURRENT_TIMESTAMP, 'mcu-bp'
),
('Avengers: Infinity War',
 'The Avengers and their allies must be willing to sacrifice all in an attempt to defeat the powerful Thanos before his blitz of devastation and ruin puts an end to the universe.',
 'The Avengers and their allies must be willing to sacrifice all in an attempt to defeat the powerful Thanos before his blitz of devastation and ruin puts an end to the universe.',
 2018, '2015-04-27', 7.2, CURRENT_TIMESTAMP, 'mcu-a-iw'
),
('Ant-Man and the Wasp',
 'As Scott Lang balances being both a superhero and a father, Hope van Dyne and Dr. Hank Pym present an urgent new mission that finds the Ant-Man fighting alongside The Wasp to uncover secrets from their past.',
 'As Scott Lang balances being both a superhero and a father, Hope van Dyne and Dr. Hank Pym present an urgent new mission that finds the Ant-Man fighting alongside The Wasp to uncover secrets from their past.',
 2018, '2018-07-06', 7.2, CURRENT_TIMESTAMP, 'mcu-amatw'
),
('Captain Marvel',
 'Carol Danvers becomes one of the universe''s most powerful heroes when Earth is caught in the middle of a galactic war between two alien races.',
 'Carol Danvers becomes one of the universe''s most powerful heroes when Earth is caught in the middle of a galactic war between two alien races.',
 2019, '2015-03-08', 7.2, CURRENT_TIMESTAMP, 'mcu-cm'
),
('Avengers: Endgame',
 'After the devastating events of Avengers: Infinity War (2018), the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos'' actions and restore balance to the universe.',
 'After the devastating events of Avengers: Infinity War (2018), the universe is in ruins. With the help of remaining allies, the Avengers assemble once more in order to reverse Thanos'' actions and restore balance to the universe.',
 2019, '2019-04-26', 7.2, CURRENT_TIMESTAMP, 'mcu-a-eg'
),
('Spider-Man: Far From Home',
 'Peter Parker heads to Europe hoping for a normal summer, but elemental attacks and a mysterious new hero pull him back into a dangerous fight he isn''t ready to embrace.',
 'Peter Parker heads to Europe hoping for a normal summer, but elemental attacks and a mysterious new hero pull him back into a dangerous fight he isn''t ready to embrace.',
 2019, '2019-07-02', 7.2, CURRENT_TIMESTAMP, 'mcu-sm-ffh'
)
;

INSERT INTO person(first_name, last_name, title, category, created_at, code) VALUES
('Jon', 'Favreau', 'Director Actor', 'CREW',CURRENT_TIMESTAMP, 'cr-jf'),
('Mark', 'Fergus', 'Writer', 'CREW', CURRENT_TIMESTAMP, 'cr-mf'),
('Hawk', 'Ostby', 'Writer', 'CREW', CURRENT_TIMESTAMP, 'cr-ho'),
('Art', 'Marcum', 'Writer', 'CREW', CURRENT_TIMESTAMP, 'cr-am'),
('Matt', 'Holloway', 'Writer', 'CREW', CURRENT_TIMESTAMP, 'cr-mh'),
('Louis', 'Leterrier', 'Director', 'CREW', CURRENT_TIMESTAMP, 'cr-ll'),
('Zak', 'Penn', 'Writer', 'CREW', CURRENT_TIMESTAMP, 'cr-zp'),
('Robert', 'Downey Jr.', 'Actor', 'CAST', CURRENT_TIMESTAMP, 'ca-rdj'),
('Gwyneth', 'Paltrow', 'Actor', 'CAST', CURRENT_TIMESTAMP, 'ca-gp'),
('Terrence', 'Howard', 'Actor', 'CAST', CURRENT_TIMESTAMP, 'ca-th'),
('Jeff', 'Bridges', 'Actor', 'CAST', CURRENT_TIMESTAMP, 'ca-jb'),
('Leslie', 'Bibb', 'Actor', 'CAST', CURRENT_TIMESTAMP, 'ca-lb'),
('Clark', 'Gregg', 'Actor', 'CAST', CURRENT_TIMESTAMP, 'ca-cg')
;

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 1, 'CREW', 'mcu-im-d'
FROM movie m, person p
WHERE m.code = 'mcu-im'
  AND p.code = 'cr-jf';

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 2, 'CREW', 'mcu-im-w'
FROM movie m, person p
WHERE m.code = 'mcu-im'
  AND p.code in ('cr-mf', 'cr-ho', 'cr-am', 'cr-mh');

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 1, 'CREW', 'mcu-tih-d'
FROM movie m, person p
WHERE m.code = 'mcu-tih'
  AND p.code = 'cr-ll';

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 2, 'CREW', 'mcu-tih-w'
FROM movie m, person p
WHERE m.code = 'mcu-tih'
  AND p.code = 'cr-zp';

INSERT INTO job_crew(job_id, job_name)
SELECT id, 'Director'
FROM job
WHERE code in ('mcu-im-d', 'mcu-tih-d');

INSERT INTO job_crew(job_id, job_name)
SELECT id, 'Writer'
FROM job
WHERE code in ('mcu-im-w', 'mcu-tih-w');

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 1, 'CAST', p.code
FROM movie m, person p
WHERE m.code = 'mcu-im'
  AND p.code = 'ca-rdj';

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 2, 'CAST', p.code
FROM movie m, person p
WHERE m.code = 'mcu-im'
  AND p.code = 'ca-gp';

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 3, 'CAST', p.code
FROM movie m, person p
WHERE m.code = 'mcu-im'
  AND p.code = 'ca-th';

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 3, 'CAST', p.code
FROM movie m, person p
WHERE m.code = 'mcu-im'
  AND p.code = 'ca-jb';

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 4, 'CAST', p.code
FROM movie m, person p
WHERE m.code = 'mcu-im'
  AND p.code = 'ca-lb';

INSERT INTO job (movie_id, person_id, sequence, job_type, code)
SELECT m.id, p.id, 4, 'CAST', p.code
FROM movie m, person p
WHERE m.code = 'mcu-im'
  AND p.code = 'ca-cg';

INSERT INTO job_cast(job_id, character_name)
SELECT id, 'Tony Stark'
FROM job
WHERE code = 'ca-rdj';

INSERT INTO job_cast(job_id, character_name)
SELECT id, 'Pepper Potts'
FROM job
WHERE code = 'ca-gp';

INSERT INTO job_cast(job_id, character_name)
SELECT id, 'James Rhodes'
FROM job
WHERE code = 'ca-th';

INSERT INTO job_cast(job_id, character_name)
SELECT id, 'Obadiah Stane'
FROM job
WHERE code = 'ca-jb';

INSERT INTO job_cast(job_id, character_name)
SELECT id, 'Christine Everhard'
FROM job
WHERE code = 'ca-lb';

INSERT INTO job_cast(job_id, character_name)
SELECT id, 'Agent Coulson'
FROM job
WHERE code = 'ca-cg';

ALTER TABLE person DROP COLUMN code;
ALTER TABLE movie DROP COLUMN code;
ALTER TABLE job DROP COLUMN code;

COMMIT;
