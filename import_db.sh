#! /usr/bin/env bash

echo importing DB

PGPASSWORD=ebooks
psql="docker run -i --rm --network mbs3_default -e PGPASSWORD=$PGPASSWORD postgres:15.3 psql"

$psql -h db ebooks ebooks < mbs2_db_backup.sql
$psql -h db ebooks ebooks < modify_mbs2_db.sql

 