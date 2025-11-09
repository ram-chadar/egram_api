package com.egram.api.entity;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author RAM
 */
@Entity
@Table(name = "system_users")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "com.egram.api.entity.SystemUserEntity")
public class SystemUserEntity extends  BaseEntity{

	@Id
	@Column(name = "user_name", unique = true, nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "question", nullable = false)
	private String question;

	@Column(name = "answer", nullable = false)
	private String answer;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "system_user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private List<RoleEntity> roles;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "systemuser_regions", joinColumns = {
			@JoinColumn(name = "system_user_id") }, inverseJoinColumns = { @JoinColumn(name = "region_id") })
	private List<RegionsEntity> regions;

	@Column(nullable = false)
	private String status="Active";

	@Column
	private String statusReason;

	@OneToOne()
	@JoinColumn(name = "dsa_id")
	private DsaApplicationEntity dsaApplicationId;

}
