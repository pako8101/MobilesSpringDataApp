package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@XmlRootElement(name = "devices")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceSeedRootDto implements Serializable {
    @XmlElement(name = "device")
    List<DeviceSeedDto>deviceSeedDtos;

    public List<DeviceSeedDto> getDeviceSeedDtos() {
        return deviceSeedDtos;
    }

    public void setDeviceSeedDtos(List<DeviceSeedDto> deviceSeedDtos) {
        this.deviceSeedDtos = deviceSeedDtos;
    }
}
