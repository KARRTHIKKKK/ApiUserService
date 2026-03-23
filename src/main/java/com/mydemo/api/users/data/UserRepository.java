package com.mydemo.api.users.data;
//Spring Data JPA Crud Repository
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long>
{

}
