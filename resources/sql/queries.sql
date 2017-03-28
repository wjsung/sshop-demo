-- :name create-user! :! :n
-- creates a new user record
INSERT INTO users
(id, pass)
VALUES (:id, :pass)

-- :name create-userall! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
WHERE id = :id

--START:product-upload
-- :name product-upload! :! :n
-- saves a product indo to the database
INSERT INTO products
(owner, name,price, description, imgpath)
VALUES (:owner, :name, :price, :description, :imgpath)
--END:upload-product

-- :name get-products :? :*
-- :doc retrieve products.
SELECT id,owner, name,price, description, imgpath FROM products

-- :name get-product :? :1
-- :doc retrieve a product given the id.
SELECT * FROM products
WHERE id = :id

