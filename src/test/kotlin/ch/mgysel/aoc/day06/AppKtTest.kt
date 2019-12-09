package ch.mgysel.aoc.day06

import io.kotlintest.shouldBe
import org.junit.Test

class AppKtTest {

    private val sample = """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            """.trimIndent()
            .lines()
            .filter(String::isNotEmpty)

    @Test
    fun sample() {
        val data = sample
                .map(::parseLine)

        val map = OrbitMap(data)
        map.countOrbits("D") shouldBe 3
        map.countOrbits("L") shouldBe 7
        map.countOrbits("COM") shouldBe 0
        map.countAllOrbits() shouldBe 42
    }

    private val santaSample = """
            COM)B
            B)C
            C)D
            D)E
            E)F
            B)G
            G)H
            D)I
            E)J
            J)K
            K)L
            K)YOU
            I)SAN
            """.trimIndent()
            .lines()
            .filter(String::isNotEmpty)

    @Test
    fun santaTransfer() {
        val data = santaSample
                .map(::parseLine)

        val map = OrbitMap(data)
        map.calculateTransfer("YOU", "SAN") shouldBe 4
    }
}