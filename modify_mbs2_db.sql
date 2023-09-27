DO $$
DECLARE seq record;
BEGIN FOR seq IN (
    SELECT c.relname AS seqname
    FROM pg_class c
    WHERE c.relkind = 'S'
    ORDER BY c.relname
) LOOP IF POSITION('_id_seq' in seq.seqname) > 0 THEN EXECUTE format(
    'ALTER SEQUENCE %I RENAME TO %I',
    seq.seqname,
    REPLACE(seq.seqname, '_id_seq', '_seq')
);
END IF;
END LOOP;
END $$;
-- 
DO $$
DECLARE seq record;
BEGIN FOR seq IN (
    SELECT c.relname AS seqname
    FROM pg_class c
    WHERE c.relkind = 'S'
    ORDER BY c.relname
) LOOP IF POSITION('_seq' in seq.seqname) > 0 THEN EXECUTE format('ALTER SEQUENCE %I INCREMENT BY 50', seq.seqname);
END IF;
END LOOP;
END $$;
--
DO
$$
DECLARE tbl record;
col_name text;
col_names text [] := ARRAY ['created_by_id', 'modified_by_id'];
BEGIN FOREACH col_name IN ARRAY col_names LOOP FOR tbl IN (
    SELECT DISTINCT table_name
    FROM information_schema.columns
    WHERE column_name = col_name
) LOOP EXECUTE format(
    'ALTER TABLE %I DROP CONSTRAINT IF EXISTS %s_%s_fkey',
    tbl.table_name,
    tbl.table_name,
    col_name
);
EXECUTE format(
    'ALTER TABLE %I ALTER COLUMN %I TYPE varchar(255)',
    tbl.table_name,
    col_name
);
EXECUTE format(
    'ALTER TABLE %I RENAME COLUMN %I TO %I',
    tbl.table_name,
    col_name,
    REPLACE(col_name, '_id', '')
);
END LOOP;
END LOOP;
END $$;
--
drop trigger if exists ebook_ts_insert on ebook;
drop trigger if exists ebook_ts_update on ebook;
alter table ebook drop column full_text;
drop trigger if exists ebook_ts_update on author;
drop trigger if exists ebook_ts_update on ebook_authors;
--

DO $$
DECLARE r RECORD;
BEGIN FOR r IN (
    SELECT routine_schema,
        routine_name
    FROM information_schema.routines
    WHERE routine_type = 'FUNCTION'
        AND specific_schema = current_schema()
        and routine_name not like 'unaccent%'
) LOOP EXECUTE 'DROP FUNCTION IF EXISTS ' || r.routine_schema || '.' || r.routine_name || ' CASCADE';
END LOOP;
END $$;