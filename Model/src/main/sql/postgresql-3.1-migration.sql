-- Migration from a 3.0 schema to a 3.1 schema

-- Move `number` column from comics to records.
alter table records add column number int;
update records r set number = (select c.number from comics c where c.id = r.id);
alter table comics drop column number;

-- Add `alive` column to records.
alter table records add column alive boolean;
update records set alive = true;
alter table records alter column alive set not null;
