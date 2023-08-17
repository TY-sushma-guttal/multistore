package com.mrmrscart.userservice.repository.supplier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mrmrscart.userservice.entity.supplier.SupplierStoreTheme;

@Repository
public interface SupplierStoreThemeRepository extends JpaRepository<SupplierStoreTheme, Integer> {

	SupplierStoreTheme findByColorName(String string);

}
