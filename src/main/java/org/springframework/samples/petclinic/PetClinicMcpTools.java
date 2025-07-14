package org.springframework.samples.petclinic;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import static lombok.AccessLevel.PACKAGE;

@Service
@RequiredArgsConstructor(access = PACKAGE)
public class PetClinicMcpTools {

	private final OwnerRepository ownerRepository;

	private final PetTypeRepository petTypeRepository;

	private final ObjectMapper objectMapper;

	@SneakyThrows
	@Tool(description = "Retrieves owners")
	String listOwners(@ToolParam(description = "Number of page (1-based indexing)") Integer pageNumber,
			@ToolParam(description = "Last name of the owner") String lastName) {
		final var pageable = PageRequest.of(pageNumber - 1, 10);
		final var owners = ownerRepository.findByLastNameStartingWith(lastName, pageable);

		return objectMapper.writeValueAsString(owners);
	}

	@SneakyThrows
	@Tool(description = "Creates a new owner")
	String createOwner(@ToolParam(description = "First name") String firstName,
			@ToolParam(description = "Last name") String lastName,
			@ToolParam(description = "Address (street, building number, postal code)") String address,
			@ToolParam(description = "City") String city, @ToolParam(description = "Telephone") String telephone) {
		final var owner = new Owner();
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
		owner.setAddress(address);
		owner.setCity(city);
		owner.setTelephone(telephone);
		ownerRepository.save(owner);

		return objectMapper.writeValueAsString(Map.of("message", "added successfully", "id", owner.getId()));
	}

	@SneakyThrows
	@Tool(description = "Creates a new pet")
	String createPet(@ToolParam(description = "Owner ID") int ownerId, @ToolParam(description = "Name") String name,
			@ToolParam(description = "Date of birth in YYYY-MM-DD format") String dateOfBirth,
			@ToolParam(description = "Pet type") int petTypeId) {
		final var petType = petTypeRepository.findById(petTypeId)
			.orElseThrow(() -> new IllegalArgumentException("Pet type with ID %s has not found".formatted(petTypeId)));

		final var pet = new Pet();
		pet.setName(name);
		pet.setBirthDate(LocalDate.parse(dateOfBirth));
		pet.setType(petType);

		final var owner = ownerRepository.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner with ID %s has not been found".formatted(ownerId)));
		owner.addPet(pet);
		ownerRepository.save(owner);

		return objectMapper.writeValueAsString(Map.of("message", "added successfully"));
	}

	@SneakyThrows
	@Tool(description = "List pets by owner")
	String listPets(@ToolParam(description = "Owner ID") int ownerId) {
		final var owner = ownerRepository.findById(ownerId)
			.orElseThrow(() -> new IllegalArgumentException("Owner with ID %s has not been found".formatted(ownerId)));

		final var pets = owner.getPets();

		return objectMapper.writeValueAsString(pets);
	}

	@SneakyThrows
	@Tool(description = "Retrieves pet types")
	String listPetTypes(@ToolParam(description = "Number of page (1-based indexing)") Integer pageNumber) {
		return objectMapper.writeValueAsString(petTypeRepository.findPetTypes());
	}

}
