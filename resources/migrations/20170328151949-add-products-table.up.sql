CREATE TABLE products
(id INTEGER PRIMARY KEY AUTO_INCREMENT,
 owner VARCHAR(30),
 name VARCHAR(30),
 price REAL, --DECIMAL(6,2),
 description VARCHAR(300),
 imgpath VARCHAR(300),
 regdate VARCHAR(30),
 editdate VARCHAR(30)
);
