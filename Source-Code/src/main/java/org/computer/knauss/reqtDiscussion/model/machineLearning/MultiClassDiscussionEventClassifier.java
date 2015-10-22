package org.computer.knauss.reqtDiscussion.model.machineLearning;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableModel;

import oerich.nlputils.classifier.machinelearning.ILearningClassifier;
import oerich.nlputils.classifier.machinelearning.NewBayesianClassifier;

import org.computer.knauss.reqtDiscussion.model.DiscussionEvent;
import org.computer.knauss.reqtDiscussion.model.IClassificationFilter;

public class MultiClassDiscussionEventClassifier implements
		IDiscussionEventClassifier {
	private static final String PRIMARY_CLASS = "clari";
	private Map<String, ILearningClassifier> classifiers = new HashMap<String, ILearningClassifier>();
	private ITrainingStrategy trainingStrat;
	private String name = getClass().getSimpleName();

	@Override
	public void trainDiscussionEvent(DiscussionEvent de) {
		trainDiscussionEvent(de, IClassificationFilter.NAME_FILTER.getName());
	}

	@Override
	public boolean inClass(DiscussionEvent de) {
		return classify(de) > getMatchValue();
	}

	@Override
	public double classify(DiscussionEvent de) {
		try {
			ILearningClassifier classifier = getClassifier(PRIMARY_CLASS);
			String stringForClassification = this.trainingStrat
					.getStringForClassification(de);
			double primClassVal = classifier.classify(stringForClassification);
			if (primClassVal < classifier.getMatchValue()) {
				return 0;
			}

			for (String otherClass : this.classifiers.keySet()) {
				if (!PRIMARY_CLASS.equals(otherClass)) {
					if (getClassifier(otherClass).isMatch(
							stringForClassification)) {
						return 0.4;
					}
				}
			}

			return primClassVal;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void setTrainingStrategy(ITrainingStrategy strat) {
		this.trainingStrat = strat;
	}

	@Override
	public ITrainingStrategy getTrainingStrategy() {
		return this.trainingStrat;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public double getMatchValue() {
		try {
			return getClassifier(PRIMARY_CLASS).getMatchValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void clear() {
		for (ILearningClassifier classifier : this.classifiers.values())
			try {
				classifier.clear();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void trainDiscussionEvent(DiscussionEvent de,
			String referenceRaterName) {
		IClassificationFilter.NAME_FILTER.setName(referenceRaterName);

		String classif = de.getReferenceClassification().substring(0, 5);
		try {
			ILearningClassifier classifier = getClassifier(classif);

			classifier.learnInClass(this.trainingStrat
					.getStringForClassification(de));

			for (String otherClass : this.classifiers.keySet()) {
				if (!classif.equals(otherClass))
					getClassifier(otherClass).learnNotInClass(
							this.trainingStrat.getStringForClassification(de));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ILearningClassifier getClassifier(String classif) throws Exception {
		ILearningClassifier ret = this.classifiers.get(classif);
		if (ret == null) {
			NewBayesianClassifier classifier = new NewBayesianClassifier();
			classifier.init(new File(classif + "-classifier.txt"));
			classifier.setAutosave(false);
			// Using stemmer or stopword filter does not really help
			// classifier.setStopWordFilter(StopWordFilterFactory.getInstance());
			// classifier.setStemmer(new Stemmer());

			// TODO try out those values:
			classifier.setProClassBias(0.8);
			classifier.setUnknownWordValue(0.3);
			
			ret = classifier;
			this.classifiers.put(classif, ret);
		}
		return ret;
	}

	@Override
	public String toString() {
		if (getName() == null)
			return getClass().getSimpleName();
		return getName();
	}

	@Override
	public void storeToFile() {
		for (ILearningClassifier classifier : this.classifiers.values())
			if (classifier instanceof NewBayesianClassifier)
				try {
					((NewBayesianClassifier) classifier).storeToFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	@Override
	public TableModel explainClassification(DiscussionEvent de) {
		try {
			return getClassifier(PRIMARY_CLASS).explainClassification(
					this.trainingStrat.getStringForClassification(de));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
