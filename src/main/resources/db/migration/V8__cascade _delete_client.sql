ALTER TABLE project DROP CONSTRAINT project_client_id_fkey;
ALTER TABLE project ADD CONSTRAINT project_client_id_fkey
    FOREIGN KEY (client_id) REFERENCES client(id) ON DELETE CASCADE;

ALTER TABLE estimate DROP CONSTRAINT estimate_project_id_fkey;
ALTER TABLE estimate ADD CONSTRAINT estimate_project_id_fkey
    FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE;

ALTER TABLE estimate_item DROP CONSTRAINT estimate_item_estimate_id_fkey;
ALTER TABLE estimate_item ADD CONSTRAINT estimate_item_estimate_id_fkey
    FOREIGN KEY (estimate_id) REFERENCES estimate(id) ON DELETE CASCADE;


