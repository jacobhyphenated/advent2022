package com.jacobhyphenated.advent2022.day13

import com.jacobhyphenated.advent2022.Day

/**
 * Day 13: Distress Signal
 *
 * Packets are coming in out of order.
 * Packets come in pairs (one on each line of the input).
 *
 * A packet consists of a number or list of packets, ex:
 *      [[1],[2,3,4]] turns into Packet[Packet[Packet[1]], Packet[Packet[2],Packet[3],Packet[4]]]
 *      [[1],4] turns into Packet[Packet[Packet[1]], Packet[4]]
 *
 */
class Day13: Day<List<Pair<Packet, Packet>>> {
    override fun getInput(): List<Pair<Packet, Packet>> {
        return parseInput(readInputFile("day13"))
    }

    /**
     * Observe each pair of packets. Find the index (1 based) in the list of
     * all the packets that are in the correct order.
     * Return the sum of those indexes
     */
    override fun part1(input: List<Pair<Packet, Packet>>): Int {
        return input.mapIndexed { index, (lhs, rhs) ->
            if (lhs <= rhs) { index + 1 } else { 0 }
        }.sum()
    }

    /**
     * Take all packets and put them in a single list. Add two marker packets: [[2]] and [[6]]
     * Put the packets in the correct order and find the (1 based) index of the marker packets.
     * Return the product of both marker indexes
     */
    override fun part2(input: List<Pair<Packet, Packet>>): Any {
        val (marker1, _) = parsePacket("[[2]]")
        val (marker2, _) = parsePacket("[[6]]")
        val allPackets = input.flatMap { listOf(it.first, it.second) } + listOf(marker1, marker2)
        val sortedPackets = allPackets.sorted()
        return (sortedPackets.indexOf(marker1) + 1) * (sortedPackets.indexOf(marker2) + 1)
    }

    fun parseInput(input: String): List<Pair<Packet, Packet>> {
        return input.split("\n\n").map { pairs ->
            val (lhs, rhs) = pairs.lines().map { parsePacket(it).first }
            Pair(lhs, rhs)
        }
    }

    /**
     * Recursive function to parse the packet out of the string.
     * Nested [] get their own recursive call.
     *
     * @param packetString The entire packet string
     * @param index the index to start the parsing at (defaults to 1 - all packets start with '[')
     * @return A pair tuple consisting of the parsed packet
     * and the index of the lasted visited location in the overall packetString
     */
    private fun parsePacket(packetString: String, index: Int = 1): Pair<Packet, Int> {
        var buffer = ""
        var i = index
        val packets = mutableListOf<Packet>()
        // when we hit the close bracket, this parse function is done
        while (packetString[i] != ']') {
            if (packetString[i] == '[') {
                // we are opening a new sub packet - recursively call this method
                val (p, newIndex) = parsePacket(packetString, i+1)
                packets.add(p)
                i = newIndex
                continue
            }
            if (packetString[i] == ',') {
                // On a comma, move to the next item in the list (clear the buffer if necessary)
                if (buffer.isNotEmpty()) {
                    packets.add(Packet(buffer.toInt(), null))
                    buffer = ""
                }
                i++
                continue
            }
            // Numbers are stored in the buffer (as multi digit numbers are valid)
            buffer += packetString[i]
            i++
        }
        if (buffer.isNotEmpty()) {
            packets.add(Packet(buffer.toInt(), null))
        }
        return Pair(Packet(null, packets), i + 1)
    }
}

class Packet (
    private val value: Int?,
    private val packets: List<Packet>?
) : Comparable<Packet> {

    /**
     * To check order, look from left to right in each packet
     *      If the value is a number on both, the left should be less than the right
     *      If one of the two packets has a number, convert that number to a single value list
     *      If both are lists, go from left to right comparing each value in the list (left-hand side should be smaller)
     *      - if the left-hand side runs out of values first, they are in the correct order
     *      - if the right-hand side runs out of values first, they are out of order
     */
    override fun compareTo(other: Packet): Int {
        if (this.value != null && other.value != null) {
            return this.value.compareTo(other.value)
        }
        val listA = this.packets ?: listOf(Packet(this.value!!, null))
        val listB = other.packets ?: listOf(Packet(other.value!!, null))
        for (i in listA.indices) {
            if (i >= listB.size) { return 1 }
            val cmp = listA[i].compareTo(listB[i])
            if (cmp != 0) { return cmp }
        }
        if (listA.size < listB.size) {
            return - 1
        }
        return 0
    }

    /**
     * We could probably cheat and just do reference equivalence for this problem
     * But here is a real equals method implementation
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) { return true }
        if (other !is Packet) { return false }
        if (other.value != null && this.value != null && this.value == other.value) {
            return true
        }
        if (other.packets != null && this.packets != null){
            if (other.packets.size != this.packets.size) { return false }
            return other.packets == this.packets
        }
        return false
    }

    // The IDE generated this one (It's always recommended to have hashCode if you have equals)
    override fun hashCode(): Int {
        val result = value ?: 0
        return 31 * result + (packets?.hashCode() ?: 0)
    }

}