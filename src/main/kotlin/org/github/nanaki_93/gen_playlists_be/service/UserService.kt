package org.github.nanaki_93.gen_playlists_be.service

import org.github.nanaki_93.gen_playlists_be.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists_be.mapper.UserMapper
import org.github.nanaki_93.gen_playlists_be.model.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) {


    fun register(createUserDto: CreateUserDto) =
        if (userRepository.findByEmail(createUserDto.email) != null) {
            throw IllegalStateException("User already exists")
        } else {
            userMapper.toDto(userRepository.save(userMapper.toEntity(createUserDto)))
        }


    fun getUserByEmail(email: String) = userRepository.findByEmail(email)

}
