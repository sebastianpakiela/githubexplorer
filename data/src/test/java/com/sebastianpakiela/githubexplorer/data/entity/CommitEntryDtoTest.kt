package com.sebastianpakiela.githubexplorer.data.entity

import com.sebastianpakiela.githubexplorer.data.entity.network.AuthorDto
import com.sebastianpakiela.githubexplorer.data.entity.network.CommitDto
import com.sebastianpakiela.githubexplorer.data.entity.network.CommitEntryDto
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CommitEntryDtoTest {

    private val dateFormatter =
        DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN).withZone(ZoneId.systemDefault())

    @Test
    fun `Should map entry to domain model`() {
        val commitEntryDto = CommitEntryDto(
            sha = "123", commit = CommitDto(
                author = AuthorDto(name = "authorname", Instant.ofEpochMilli(0)), message = "Message"
            )
        )

        val result = commitEntryDto.toDomain(dateFormatter)

        assertThat(result.date, equalTo("01:00:00 01.01.1970"))
        assertThat(result.sha, equalTo(commitEntryDto.sha))
        assertThat(result.message, equalTo(commitEntryDto.commit!!.message))
        assertThat(result.author, equalTo(commitEntryDto.commit!!.author.name))
    }
}