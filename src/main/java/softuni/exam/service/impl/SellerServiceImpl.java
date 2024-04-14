package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.SellerSeedDto;
import softuni.exam.models.entity.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;

    private final ValidationUtil validationUtil;

private static final String PATH = "src/main/resources/files/json/sellers.json";

    public SellerServiceImpl(SellerRepository sellerRepository,
                             ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count()>0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(Path.of(PATH));
    }

    @Override
    public String importSellers() throws IOException {
        StringBuilder sb = new StringBuilder();
        SellerSeedDto[] sellerSeedDtos = this.gson.
                fromJson(readSellersFromFile(), SellerSeedDto[].class);


        Arrays.stream(sellerSeedDtos)
                .filter(sellerSeedDto -> {
                    boolean isValid = validationUtil.isValid(sellerSeedDto);
                    Optional<Seller>optSeller = this.sellerRepository
                            .findByFirstName(sellerSeedDto.getFirstName());
                    if (optSeller.isPresent()){
                        isValid=false;
                    }

                    sb.append(isValid ? String.format("Successfully imported seller %s %s",
                                    sellerSeedDto.getFirstName(), sellerSeedDto.getLastName()) :
                                    "Invalid seller")
                            .append(System.lineSeparator());
                    return isValid;

                }).
                map(sellerSeedDto -> modelMapper.map(sellerSeedDto, Seller.class))
                .forEach(sellerRepository::save);

        return sb.toString().trim();
    }
}
