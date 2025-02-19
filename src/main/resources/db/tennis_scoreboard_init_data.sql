insert into t_players(c_name)
values ('Max'),
       ('Dima'),
       ('Oleg');

insert into t_matches(c_first_player_id, c_second_player_id, c_winner_id)
values ( 1, 2, 1 ),
       (2, 3, 2),
       (1, 3, 3);