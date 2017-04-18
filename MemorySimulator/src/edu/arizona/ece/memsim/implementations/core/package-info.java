/**
 * Example Implementation of a Memory Simulation
 * <P>
 * This Example Uses the Following Designs:
 * <P>
 *   Program Design:<BR>
 * 	   L1: 8KB, 32B Blocks, Fully Associative, 1 Cycle Access Time<BR>
 *     L2: 128KB, 64B Blocks, 16 Way Associative, 20 Cycle Access Time<BR>
 *     Memory: 512MB, 200 Cycle Access Time<BR>
 * <P>
 * 	 ProgramSmall Design<BR>
 *     L1: 1KB, 32B, Fully Associative, 1 Cycle Access Time<BR>
 *     L2: 4KB, 128B, 16 Way Associative, 20 Cycle Access Time<BR>
 *     Memory: 16KB, 200 Cycle Access Time<BR>
 * <P>
 *   Test Design:<BR>
 *     128MB Sequential Memory Access (Memory Address 0 then 1 then 2 then...)<BR>
 * <P>
 * Known Issues:<BR>
 *   2. Only Sequential and Random Memory Access Pattern Has Been Implemented and (Partially) Tested<BR>
 *   3. Only LRU Replacement Policy is Implemented (Do we want to implement Random and Round Robin?)<BR>
 *     a. LRU is Implemented<BR>
 */
package edu.arizona.ece.memsim.implementations.core;