package ai.aitia.demo.car_provider.database;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import com.aitia.demo.dto.CarResponseDTO;

import ai.aitia.demo.car_provider.entity.Car;

public class DTOConverter {

	
	//=================================================================================================
	// methods
	
	//-------------------------------------------------------------------------------------------------
	public static CarResponseDTO convertCarToCarResponseDTO(final Car car) {
		Assert.notNull(car, "car is null");
		return new CarResponseDTO(car.getId(), car.getBrand(), car.getColor());
	}
	
	//-------------------------------------------------------------------------------------------------
	public static List<CarResponseDTO> convertCarListToCarResponseDTOList(final List<Car> cars) {
		Assert.notNull(cars, "car list is null");
		final List<CarResponseDTO> carResponse = new ArrayList<>(cars.size());
		for (final Car car : cars) {
			carResponse.add(convertCarToCarResponseDTO(car));
		}
		return carResponse;
	}

	//=================================================================================================
	// assistant methods

	//-------------------------------------------------------------------------------------------------
	public DTOConverter() {
		throw new UnsupportedOperationException(); 
	}
}
