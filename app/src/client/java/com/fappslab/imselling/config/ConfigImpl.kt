package com.fappslab.imselling.config

import javax.inject.Inject

class ConfigImpl
@Inject
constructor(
) : Config {

    override val isAdmin: Boolean = false
}
