package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.DeviceSeedRootDto;
import softuni.exam.models.entity.Device;
import softuni.exam.models.entity.DeviceType;
import softuni.exam.models.entity.Sale;
import softuni.exam.repository.DeviceRepository;
import softuni.exam.service.DeviceService;
import softuni.exam.service.SaleService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static softuni.exam.models.entity.DeviceType.SMART_PHONE;

@Service
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;

    private final ValidationUtil validationUtil;
    private final SaleService saleService;

    private static final String PATH = "src/main/resources/files/xml/devices.xml";

    public DeviceServiceImpl(DeviceRepository deviceRepository,
                             ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, SaleService saleService) {
        this.deviceRepository = deviceRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.saleService = saleService;
    }

    @Override
    public boolean areImported() {
        return deviceRepository.count() > 0;
    }

    @Override
    public String readDevicesFromFile() throws IOException {
        return Files.readString(Path.of(PATH));
    }

    @Override
    public String importDevices() throws IOException, JAXBException {

        StringBuilder sb = new StringBuilder();

        xmlParser
                .fromFile(PATH, DeviceSeedRootDto.class)
                .getDeviceSeedDtos()
                .stream()
                .filter(deviceSeedDto -> {
                    boolean isValid = validationUtil.isValid(deviceSeedDto);


                    Sale sale = saleService.findSaleById(deviceSeedDto
                            .getSale());

                    if (sale == null) {
                        isValid = false;
                    }

                    Device device = deviceRepository.findByBrandAndModel(deviceSeedDto
                                    .getBrand(),
                            deviceSeedDto.getModel()).orElse(null);
                    if (device != null) {
                        isValid = false;
                    }

                    sb
                            .append(isValid
                                    ? String.format("Successfully imported device of type %s " +
                                            "with brand %s",
                                    deviceSeedDto.getDeviceType(),
                                    deviceSeedDto.getBrand())
                                    : "Invalid device")
                            .append(System.lineSeparator());

                    return isValid;
                })
                .map(deviceSeedDto -> {

                    Device device = modelMapper.map(deviceSeedDto, Device.class);

                    Sale sale = saleService.findSaleById(deviceSeedDto
                            .getSale());
                    saleService.addAndSaveAddedDevice(sale, device);

                    device.setSale(sale);
                    return device;
                })
                .forEach(deviceRepository::saveAndFlush);


        return sb.toString();

    }

    @Override
    public String exportDevices() {
        StringBuilder sb = new StringBuilder();

        Set<Device> deviceSet = deviceRepository
                .findByDeviceTypeIsAndStorageGreaterThanEqualAndPriceIsLessThanOrderByBrandAsc();
        deviceSet.forEach(device -> {
            sb.append(String.format(
//            "Device brand: %s\n,*Model: %s\n,**Storage: %d,***Price: %.2f",
//                            "Device brand: %s\n" +
//                                    "   *Model: %s\n" +
//                                    "   **Storage: %d\n" +
//                                    "   ***Price: %.2f",
                            "Device brand: %s\n" +
                                    "   *Model: %s\n" +
                                    "   **Storage: %d\n" +
                                    "   ***Price: %.2f",
                            device.getBrand(),
                            device.getModel(),
                            device.getStorage(),
                            device.getPrice()))
                    .append(System.lineSeparator());
        });


        return sb.toString().trim();
    }
}
