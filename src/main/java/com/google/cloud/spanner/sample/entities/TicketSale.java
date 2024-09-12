/*
 * Copyright 2019-2024 Google LLC
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package com.google.cloud.spanner.sample.entities;

import com.google.cloud.spanner.hibernate.PooledBitReversedSequenceStyleGenerator;
import com.google.cloud.spanner.hibernate.types.SpannerStringArray;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

/** {@link TicketSale} shows how to use bit-reversed sequences to generate primary key values. */
@Entity
public class TicketSale extends AbstractEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketSaleId")
  @GenericGenerator(
      name = "ticketSaleId",
      // Use this custom strategy to ensure the use of a bit-reversed sequence that is compatible
      // with batching multiple inserts. See also
      // https://docs.jboss.org/hibernate/orm/6.3/userguide/html_single/Hibernate_User_Guide.html#batch.
      type = PooledBitReversedSequenceStyleGenerator.class,
      parameters = {
        // Use a separate sequence name for each entity.
        @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "ticket_sale_seq"),
        // The increment_size is not actually set on the sequence that is created, but is used to
        // generate a SELECT query that fetches this number of identifiers at once.
        @Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "200"),
        @Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "50000"),
        // Add any range that should be excluded by the generator if your table already
        // contains existing values that have been generated by other generators.
        @Parameter(
            name = PooledBitReversedSequenceStyleGenerator.EXCLUDE_RANGE_PARAM,
            value = "[1,1000]"),
      })
  private long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private Concert concert;

  private String customerName;

  private BigDecimal price;

  @Type(SpannerStringArray.class)
  private List<String> seats;

  public TicketSale() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Concert getConcert() {
    return concert;
  }

  public void setConcert(Concert concert) {
    this.concert = concert;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public List<String> getSeats() {
    return seats;
  }

  public void setSeats(List<String> seats) {
    this.seats = seats;
  }
}