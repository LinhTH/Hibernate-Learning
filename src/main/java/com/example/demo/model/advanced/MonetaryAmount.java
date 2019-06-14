package com.example.demo.model.advanced;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * This value-typed class should be java.io.Serializable: when Hibernate stores entity instance data in the shared second-level cache (see section 20.2), it disassembles the
entity’s state. If an entity has a MonetaryAmount property, the serialized representation of the property value is stored in the second-level cache region. When entity data is
retrieved from the cache region, the property value is deserialized and reassembled.
 * @author thlin
 *
 */
public class MonetaryAmount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final BigDecimal value;
	protected final Currency currency;

	public MonetaryAmount(BigDecimal value, Currency currency) {
		this.value = value;
		this.currency = currency;
	}

	public BigDecimal getValue() {
		return value;
	}

	public Currency getCurrency() {
		return currency;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof MonetaryAmount))
			return false;
		final MonetaryAmount monetaryAmount = (MonetaryAmount) o;
		if (!value.equals(monetaryAmount.value))
			return false;
		if (!currency.equals(monetaryAmount.currency))
			return false;
		return true;
	}

	public int hashCode() {
		int result;
		result = value.hashCode();
		result = 29 * result + currency.hashCode();
		return result;
	}

	public String toString() {
		return getValue() + " " + getCurrency();
	}

	public static MonetaryAmount fromString(String s) {
		String[] split = s.split(" ");
		return new MonetaryAmount(new BigDecimal(split[0]), Currency.getInstance(split[1]));
	}
}
