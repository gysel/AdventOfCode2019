package ch.mgysel.aoc.common

fun <A, B> cartesianProduct(listA: Iterable<A>,
                                    listB: Iterable<B>): Sequence<Pair<A, B>> =
        sequence {
            listA.forEach { a ->
                listB.forEach { b ->
                    yield(a to b)
                }
            }
        }