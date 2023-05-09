package com.vrickey123.network.local

class FakeMetDatabase: MetDatabase {
    override fun metObjectDAO(): MetObjectDAO = FakeMetObjectDao()
}