package org.evosuite.continuous.job;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Definition of a "job", ie a run of EvoSuite on a CUT.
 * 
 * <p>
 * Note: this class is/should be immutable
 * 
 * @author arcuri
 * 
 */
public class JobDefinition {

	/**
	 * counter used to create unique ids in a thread-safe manner
	 */
	private static final AtomicInteger counter = new AtomicInteger(0);

	/**
	 * A unique, human-readable identifier for this job
	 */
	public final int jobID;

	/**
	 * define for how long this job should be run
	 */
	public final int seconds;

	/**
	 * define how much memory this job can allocate
	 */
	public final int memoryInMB;

	/**
	 * full qualifying name of the class under test (CUT)
	 */
	public final String cut;

	/**
	 * the configuration id, identify which parameter settings were used
	 */
	public final int configurationId;

	/**
	 * the name of all classes this CUT depends on, and that would be good to
	 * have generated test cases before starting this job. This is a union of
	 * all the the types of dependency (eg, input and parent)
	 */
	public final Set<String> dependentOnClasses;

	/**
	 * All dependent classes used as input for this CUT
	 */
	public final Set<String> inputClasses;

	/**
	 * All dependent classes in the parent hierarchy
	 */
	public final Set<String> parentClasses;

	/**
	 * Main constructor
	 * 
	 * @param seconds
	 * @param memoryInMB
	 * @param cut
	 * @param configurationId
	 */
	public JobDefinition(int seconds, int memoryInMB, String cut, int configurationId,
	        Set<String> inputDependencies, Set<String> parentDependencies) {
		super();
		this.jobID = counter.getAndIncrement();
		this.seconds = seconds;
		this.memoryInMB = memoryInMB;
		this.cut = cut;
		this.configurationId = configurationId;

		HashSet<String> union = new HashSet<String>();

		if (inputDependencies != null && inputDependencies.size() > 0) {
			this.inputClasses = Collections.unmodifiableSet(new HashSet<String>(
			        inputDependencies));
			union.addAll(inputClasses);
		} else {
			this.inputClasses = null;
		}

		if (parentDependencies != null && parentDependencies.size() > 0) {
			this.parentClasses = Collections.unmodifiableSet(new HashSet<String>(
			        parentDependencies));
			union.addAll(parentClasses);
		} else {
			this.parentClasses = null;
		}

		if (union.size() == 0) {
			this.dependentOnClasses = null;
		} else {
			this.dependentOnClasses = Collections.unmodifiableSet(new HashSet<String>(
			        union));
		}
	}

	/**
	 * Create a copy of this job, and add the input and parent dependencies to
	 * the set of CUT dependencies
	 * 
	 * <p>
	 * It is OK to have one of the sets null, but not both
	 * 
	 * @param input
	 * @return
	 */
	public JobDefinition getByAddingDependencies(Set<String> inputs, Set<String> parents)
	        throws IllegalArgumentException {

		if (inputs == null && parents == null) {
			throw new IllegalArgumentException("Both sets are null");
		}

		if (inputClasses != null) {
			if (inputs == null) {
				inputs = inputClasses;
			} else {
				inputs.addAll(inputClasses);
			}
		}
		if (parentClasses != null) {
			if (parents == null) {
				parents = parentClasses;
			} else {
				parents.addAll(parentClasses);
			}
		}

		return new JobDefinition(seconds, memoryInMB, cut, configurationId, inputs,
		        parents);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + configurationId;
		result = prime * result + ((cut == null) ? 0 : cut.hashCode());
		result = prime * result
		        + ((dependentOnClasses == null) ? 0 : dependentOnClasses.hashCode());
		result = prime * result + jobID;
		result = prime * result + memoryInMB;
		result = prime * result + seconds;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobDefinition other = (JobDefinition) obj;
		if (configurationId != other.configurationId)
			return false;
		if (cut == null) {
			if (other.cut != null)
				return false;
		} else if (!cut.equals(other.cut))
			return false;
		if (dependentOnClasses == null) {
			if (other.dependentOnClasses != null)
				return false;
		} else if (!dependentOnClasses.equals(other.dependentOnClasses))
			return false;
		if (jobID != other.jobID)
			return false;
		if (memoryInMB != other.memoryInMB)
			return false;
		if (seconds != other.seconds)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return jobID + ": " + cut;
	}

}
