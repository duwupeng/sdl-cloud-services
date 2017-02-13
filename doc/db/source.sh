#!/usr/bin/env bash

cat accountdb.sql admindb.sql commondb.sql  examerdb.sql questiondb.sql  >> database_setup.sql

cat  adminDb_init_data.sql commonDb_init_data.sql  examerDb_init_data.sql 20170229/adminDb_init_data.sql >> init_data.sql
