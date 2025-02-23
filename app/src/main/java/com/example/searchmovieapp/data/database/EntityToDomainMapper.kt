package com.example.searchmovieapp.data.database

interface EntityToDomainMapper<Entity, Domain> {

    fun asDomainFromDb(entity: Entity): Domain

    fun asEntity(domain: Domain): Entity

}

interface ResponseToEntityMapper<Response, Entity> {

    fun asEntityFromRemote(response: Response): Entity
}

interface ResponseToDomainMapper<Response, Domain> {

    fun asDomainFromRemote(response: Response): Domain
}