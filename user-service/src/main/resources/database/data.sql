--INSERT INTO roles ( role ) VALUES ( 'ADMIN' );
--INSERT INTO roles ( role ) VALUES ( 'USER' );


INSERT INTO roles (role)
    SELECT 'ROLE_ADMIN' FROM DUAL WHERE NOT EXISTS (
        SELECT role FROM roles WHERE role = 'ROLE_ADMIN');

INSERT INTO roles (role)
    SELECT 'ROLE_USER' FROM DUAL WHERE NOT EXISTS (
        SELECT role FROM roles WHERE role = 'ROLE_USER');