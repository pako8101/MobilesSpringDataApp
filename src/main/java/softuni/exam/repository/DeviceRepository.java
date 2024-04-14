package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Device;
import softuni.exam.models.entity.DeviceType;

import java.util.Optional;
import java.util.Set;

@Repository
public interface DeviceRepository  extends JpaRepository<Device,Long> {


    Optional<Device> findByBrandAndModel(String brand, String model);
    @Query(value = "SELECT * FROM devices as d WHERE price < 1000" +
            " AND device_type='SMART_PHONE'   AND storage >= 128 ORDER BY LOWER(brand) ", nativeQuery = true)

    Set<Device> findByDeviceTypeIsAndStorageGreaterThanEqualAndPriceIsLessThanOrderByBrandAsc();



}
