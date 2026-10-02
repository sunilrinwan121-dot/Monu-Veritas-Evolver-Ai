package com.monu.mobile.feature.projects

import com.monu.mobile.domain.model.MONUProject
import com.monu.mobile.domain.model.MONUProjectStatus

class MONUProjectCenter {

    private val projects = mutableListOf<MONUProject>()

    fun addProject(project: MONUProject) {
        projects.add(0, project)
    }

    fun allProjects(): List<MONUProject> {
        return projects.toList()
    }

    fun activeProjects(): List<MONUProject> {
        return projects.filter {
            it.status == MONUProjectStatus.ACTIVE
        }
    }

    fun findProject(id: String): MONUProject? {
        return projects.find {
            it.id == id
        }
    }
}
