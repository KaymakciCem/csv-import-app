--- document column and related trigger is added to perform full-text search based on the title and author columns

ALTER TABLE ted.ted_talk
    ADD COLUMN document tsvector;

CREATE OR REPLACE FUNCTION update_document()
RETURNS trigger AS $$
BEGIN
    NEW.document := to_tsvector('english', COALESCE(NEW.title, '') || ' ' || COALESCE(NEW.author, ''));
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_document
    BEFORE INSERT OR UPDATE ON ted.ted_talk
                         FOR EACH ROW
                         EXECUTE FUNCTION update_document();