/**
 * 
 */
package com.example.spring.data.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;

import com.example.spring.data.jdbc.dto.Address;

/**
 * This automatically participates in transaction created by upper layer
 * (service) if any, since it uses JDbcTemplate which intern uses
 * DataSourceUtils to get connection. Read DataSourceUtils for more information.
 * 
 * @author amit
 *
 */
@Repository
public class JdbcAddressDao implements AddressDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcAddressDao.class);
	private static final String ADDRESS_TABLE_NAME = "address";
	private SimpleJdbcInsertOperations insertOperations;
	private JdbcOperations jdbcOperations;

	@Autowired
	public JdbcAddressDao(DataSource dataSource, JdbcTemplate jdbcTemplate) {
		this.insertOperations = new SimpleJdbcInsert(dataSource).withTableName(ADDRESS_TABLE_NAME);
		this.jdbcOperations = jdbcTemplate;
	}

	@Override
	public Address add(Address address) {
		if (address == null) {
			throw new InsufficientDataException();
		}
		String addressId = UUID.randomUUID().toString();

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		mapSqlParameterSource.addValue("id", addressId);
		mapSqlParameterSource.addValue("address", address.getAddress());
		mapSqlParameterSource.addValue("locality", address.getLocality());
		mapSqlParameterSource.addValue("region", address.getRegion());
		mapSqlParameterSource.addValue("city", address.getCity());
		mapSqlParameterSource.addValue("state", address.getState());
		mapSqlParameterSource.addValue("country", address.getCountry());
		mapSqlParameterSource.addValue("zipcode", address.getZipcode());
		mapSqlParameterSource.addValue("employee_id", address.getEmployeeId());

		int insertCount = insertOperations.execute(mapSqlParameterSource);
		LOGGER.info("Address created with ID :: {}, count :: {}, for employeeId :: {}",
				new Object[] { addressId, insertCount, address.getEmployeeId() });
		address.setId(addressId);
		return address;
	}

	@Override
	public Optional<Address> get(String addressId) {
		Address result = null;
		try {
			result = jdbcOperations.queryForObject(SqlStore.FIND_ADDRESS_BY_ID, this::addressRowMapper, addressId);
		} catch (EmptyResultDataAccessException e) {
			// Nothing to handle
		}
		return Optional.ofNullable(result);
	}

	private Address addressRowMapper(ResultSet rs, int rowNum) throws SQLException {
		Address address = new Address();
		address.setAddress(rs.getString("address"));
		address.setCity(rs.getString("city"));
		address.setCountry(rs.getString("country"));
		address.setLocality(rs.getString("locality"));
		address.setRegion(rs.getString("region"));
		address.setState(rs.getString("state"));
		address.setZipcode(rs.getString("zipcode"));
		return address;
	}

	@Override
	public List<Address> findBymployeeId(String employeeId) {
		return jdbcOperations.query(SqlStore.FIND_ADDRESS_BY_EMPLOYEE_ID, this::addressRowMapper, employeeId);
	}
}
