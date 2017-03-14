/**
 * Example Implementation of a Memory Simulation
 * <P>
 * This Example Uses the Following Designs:
 * <P>
 *   Computer Design:<BR>
 * 	   L1: 8KB, 64B Blocks, Fully Associative, 1 Cycle Access Time<BR>
 *     L2: 128KB, 64B Blocks, 16 Way Associative, 20 Cycle Access Time<BR>
 *     Memory: 512MB, 64B Blocks, 16 Way Associative, 200 Cycle Access Time<BR>
 * <P>
 *   Test Design:<BR>
 *     128MB Sequential Memory Access (Memory Address 0 then 1 then 2 then...)<BR>
 * <P>
 * Known Issues:<BR>
 *   1. Block Sizes Must Be the Same Across all Cache and Memories (Mike will be working on this)<BR>
 *   2. Only Sequential Memory Access Pattern Has Been Implemented and (Partially) Tested (Mike will be working on this)<BR>
 *   3. Only LRU Replacement Policy is Implemented (Do we want to implement Random and Round Robin?)<BR>
 *     a. LRU is Partially Implemented<BR>
 *     b. Statistics May Not Be Completely Accurate<BR>
 */
package edu.arizona.ece.memsim.implementations.core;