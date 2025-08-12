package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists.mapper.UserMapper
import org.github.nanaki_93.gen_playlists.repository.UserRepository
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

}