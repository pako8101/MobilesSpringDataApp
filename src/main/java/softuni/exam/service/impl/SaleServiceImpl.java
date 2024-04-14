package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.SalesSeedDto;
import softuni.exam.models.entity.Device;
import softuni.exam.models.entity.Sale;
import softuni.exam.repository.DeviceRepository;
import softuni.exam.repository.SaleRepository;
import softuni.exam.service.SaleService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final DeviceRepository deviceRepository;

    private final ValidationUtil validationUtil;

    private static final String PATH = "src/main/resources/files/json/sales.json";

    public SaleServiceImpl(SaleRepository saleRepository, ModelMapper modelMapper, Gson gson, DeviceRepository deviceRepository, ValidationUtil validationUtil) {
        this.saleRepository = saleRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.deviceRepository = deviceRepository;
        this.validationUtil = validationUtil;
    }


    @Override
    public boolean areImported() {
        return this.saleRepository.count()>0;
    }

    @Override
    public String readSalesFileContent() throws IOException {
        return Files.readString(Path.of(PATH));
    }

    @Override
    public String importSales() throws IOException {
        StringBuilder sb = new StringBuilder();
        SalesSeedDto[] salesSeedDtos = this.gson.
                fromJson(readSalesFileContent(), SalesSeedDto[].class);


        Arrays.stream(salesSeedDtos)
                .filter(salesSeedDto -> {
                    boolean isValid = validationUtil.isValid(salesSeedDto);
                    Optional<Sale> sale = this.saleRepository.getSaleByNumber(salesSeedDto.getNumber());
                    if (sale.isPresent()){
                        isValid =false;
                    }

                    sb.append(isValid ? String.format("Successfully imported sale with number %s",
                                    salesSeedDto.getNumber()) :
                                    "Invalid sale")
                            .append(System.lineSeparator());
                    return isValid;

                }).
                map(salesSeedDto -> modelMapper.map(salesSeedDto, Sale.class))
                .forEach(saleRepository::saveAndFlush);

        return sb.toString().trim();
    }

    @Override
    public Sale findSaleById(long sale) {
        return saleRepository.findById(sale).orElse(null);
    }

    @Override
    public void addAndSaveAddedDevice(Sale sale, Device device) {
        device.setSale(sale);
        this.deviceRepository.saveAndFlush(device);
    }
}

