package br.com.rotas.develop.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.maps.errors.ApiException;

import br.com.rotas.develop.model.dto.AddressDto;
import br.com.rotas.develop.model.form.AddressForm;
import br.com.rotas.develop.service.GeolocationService;


@RestController
public class GeolocationController {

	@Autowired
	private GeolocationService geoServ;
	
	@GetMapping("distancia")
	public String calculateDistance(@RequestParam("enderecos") String addressInput) throws NullPointerException {
		
		if(addressInput.isEmpty()){ 
			return "Para consultar as distâncias é necessário enviar dois ou mais endereços.";
		}
		
		try {
			
			List<AddressForm> addresses = geoServ.findLatLng(addressInput);
			if(addresses.isEmpty()){
				return "não foi possível encontrar a latitude e longitude dos endereços.";
			}
			
			List<AddressDto> dto =  geoServ.calculateDistance(addresses);
			
			return  geoServ.addRecommendation(dto);
						
		} catch (ApiException e) {
			e.printStackTrace();
			return e.toString();
		} catch (InterruptedException e) {
			e.printStackTrace();	
			return e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return e.toString();
		}
	}
}
