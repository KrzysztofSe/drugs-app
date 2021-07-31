package com.krzysztofse.drugs.drugs.repository;

import com.krzysztofse.drugs.drugs.repository.model.DrugDocument;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DrugsRepository extends PagingAndSortingRepository<DrugDocument, String> {}
