CREATE TABLE customer_roles(
	`customer_id` INT NOT NULL,
	`roles` VARCHAR(255) NOT NULL,
	foreign key (customer_id) references customer(id)
);
