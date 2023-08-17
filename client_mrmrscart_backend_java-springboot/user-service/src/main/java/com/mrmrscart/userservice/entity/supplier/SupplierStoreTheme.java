package com.mrmrscart.userservice.entity.supplier;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_supplier_store_theme")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplierStoreTheme {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int storeThemeId;
	private String colorName;
	private String colorCode;

}
