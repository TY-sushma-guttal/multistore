package com.mrmrscart.orderspaymentsservice.entity.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.mrmrscart.orderspaymentsservice.entity.earning.Earnings;
import com.mrmrscart.orderspaymentsservice.entity.payment.PaymentInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mmc_orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfo {

	@Id
	@Column(length = 45)
	private String orderId;
	
	private String orderStatus;
	
	private double orderAmount;
	
	private LocalDateTime orderDate;
	
	private double marginAmount;
	
	private LocalDateTime deliveredDate;
	
	private EUserType productOwnerType;
	
	@Column(length = 45)
	private String productOwnerId;
	
	private EUserType orderedByType;
	
	@Column(length = 45)
	private String orderedById;
	
	private EUserType orderedStoreType;
	
	@Column(length = 45)
	private String orderedStoreOwnerId;
	
	@Column(length = 45)
	private String orderedStoreProductId;
	
	private LocalDate expectedDispatchDate;
	
	private double weightInclusivePackage;
	
	private int orderQuantity;
	
	@Column(length = 35)
	private String skuId;
	
	@Column(length = 45)
	private String productId;
	
	private Long billingAddressId;
	
	private Long shippingAddressId;

	private Long pickupAddressId;
	
	private Long returnAddressId;
	
	private double discountAmount;
	
	@Column(length = 20)
	private String orderType;
	
	@Column(length = 45)
	private String profileId;
	
	private String modeOfOrder;

	@OneToOne(cascade = CascadeType.ALL)
	private InvoiceData invoiceData;

	@OneToOne(cascade = CascadeType.ALL)
	private ManifestData manifestData;

	@OneToOne(cascade = CascadeType.ALL)
	private DeliveryChargesBearer deliveryChargesBearer;

	@OneToOne(cascade = CascadeType.ALL)
	private PaymentInfo paymentInfo;

	@OneToOne(cascade = CascadeType.ALL)
	private Earnings earning;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private List<LogisticInfo> logisticInfo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private List<OrderStatus> orderStatusInfo;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "order_id")
	private List<OrderQuantityInfo> orderQuantityInfo;
}
