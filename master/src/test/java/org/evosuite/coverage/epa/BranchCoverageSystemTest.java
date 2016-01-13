/**
 * Copyright (C) 2010-2015 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser Public License as published by the
 * Free Software Foundation, either version 3.0 of the License, or (at your
 * option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License along
 * with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.coverage.epa;

import static org.junit.Assert.assertTrue;

import org.evosuite.EvoSuite;
import org.evosuite.Properties;
import org.evosuite.Properties.StoppingCondition;
import org.evosuite.SystemTest;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.testcase.TestCase;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.examples.with.different.packagename.epa.ListItr;

public class BranchCoverageSystemTest extends SystemTest {

	@Before
	public void prepare() {
		Properties.TIMEOUT = 15 * 60 * 1000; // test execution timeout 15 min
		// Properties.STRATEGY = Strategy.ENTBUG;
		// Properties.STOPPING_CONDITION = StoppingCondition.MAXTIME;
		// Properties.SEARCH_BUDGET = 60;
		//
		// Properties.TEST_ARCHIVE = false;
		// Properties.TEST_FACTORY = TestFactory.RANDOM;
		Properties.MINIMIZE = false;
		// Properties.MINIMIZE_VALUES = false;
		// Properties.INLINE = false;
		Properties.ASSERTIONS = false;
		// Properties.USE_EXISTING_COVERAGE = false;
	}

	@Test
	public void testBranchOnlyCoverage() {
		Properties.CRITERION = new Properties.Criterion[] { Properties.Criterion.BRANCH };
		Properties.STOPPING_CONDITION = StoppingCondition.MAXTIME;
		Properties.SEARCH_BUDGET = 60;
		
		// check test case
		final String targetClass = ListItr.class.getCanonicalName();
		Properties.TARGET_CLASS = targetClass;
	
		final EvoSuite evoSuite = new EvoSuite();
		String[] command = new String[] { "-generateSuite", "-class", targetClass };
		final Object results = evoSuite.parseCommandLine(command);
		Assert.assertNotNull(results);
		GeneticAlgorithm<?> ga = getGAFromResult(results);
	
		TestSuiteChromosome bestIndividual = (TestSuiteChromosome) ga.getBestIndividual();
		assertTrue(!bestIndividual.getTests().isEmpty());
	
		TestCase test = bestIndividual.getTests().get(0);
		assertTrue(!test.isEmpty());
	
		String individual = bestIndividual.toString();
		System.out.println("===========================");
		System.out.println("Best Individual:");
		System.out.println(individual);
		System.out.println("===========================");
	}

}
