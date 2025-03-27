package hnqd.aparmentmanager.incidentservice.repository;

import hnqd.aparmentmanager.incidentservice.model.FireAlert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FireAlertRepository extends MongoRepository<FireAlert, String> {
}
