package continuity.experimentation.action;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.continuity.experimentation.action.RandomSelection;
import org.continuity.experimentation.data.IDataHolder;
import org.continuity.experimentation.data.SimpleDataHolder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Henning Schulz
 *
 */
public class RandomSelectionTest {

	private RandomSelection<String> selection;

	private IDataHolder<List<String>> inputHolder;
	private IDataHolder<String> outputHolder;

	@Before
	public void setup() {
		List<String> inputList = Arrays.asList("first", "second", "third", "fourth");
		inputHolder = new SimpleDataHolder<>("input", inputList);

		outputHolder = new SimpleDataHolder<>("output", String.class);
	}

	@Test
	public void testWithTwiceInARow() {
		selection = new RandomSelection<>(inputHolder, outputHolder, false);

		Set<String> selected = new HashSet<>();

		for (int i = 0; i < 100; i++) {
			selection.execute();
			assertThat(outputHolder.get()).isIn(inputHolder.get());

			selected.add(outputHolder.get());
		}

		assertThat(selected).containsExactlyInAnyOrder(inputHolder.get().toArray(new String[] {}));
	}

	@Test
	public void testWithoutTwiceInARow() {
		selection = new RandomSelection<>(inputHolder, outputHolder, true);

		boolean first = true;
		String last = null;

		for (int i = 0; i < 100; i++) {
			selection.execute();
			assertThat(outputHolder.get()).isIn(inputHolder.get());

			if (first) {
				first = false;
			} else {
				assertThat(outputHolder.get()).isNotEqualTo(last);
			}

			last = outputHolder.get();
		}
	}

	@Test
	public void testWithoutTwiceInARowAndOneElement() {
		inputHolder = new SimpleDataHolder<>("input", Arrays.asList("first"));
		selection = new RandomSelection<>(inputHolder, outputHolder, true);

		for (int i = 0; i < 100; i++) {
			selection.execute();
			assertThat(outputHolder.get()).isEqualTo("first");
		}
	}

}
