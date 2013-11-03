package dataMiningGUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;

public class GUI extends JFrame implements WindowInterface{

	private JPanel contentPane;
	private JTextField tfPreprocessingDataInput;
	private JTextField tfPreprocessingSaveLocation;
	private JTextArea taPreprocessingSummary;
	private ControllerInterface controller = new Controller();
	private String dataset;
	private String saveLocation;
	private JTextField tfDataMiningTrainingInput;
	private JTextField tfDataMiningRuleOutput;
	private JTextField tfPostProcessingRuleInput;
	private JTextField tfPostProcessingDataFileInput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		//Construct controller
		
		
		setTitle("OCR Using Genetic Algorithms - Adam Rains, Bryan Quill - 2013");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 701, 491);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel jpPreProcessing = new JPanel();
		tabbedPane.addTab("Preprocessing", null, jpPreProcessing, null);
		
		JButton btnPreProcessingStart = new JButton("Start");
		btnPreProcessingStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String log = controller.preProcessData(dataset, saveLocation);
				taPreprocessingSummary.setText(log);
			}
		});
		
		tfPreprocessingDataInput = new JTextField();
		tfPreprocessingDataInput.setEditable(false);
		tfPreprocessingDataInput.setColumns(10);
		
		JButton btnPreProcessingSelectInput = new JButton("Select Dataset");
		btnPreProcessingSelectInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Choose file
				final JFileChooser fileChooser = new JFileChooser();
				
				int returnVal = fileChooser.showOpenDialog(GUI.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					dataset = file.getAbsolutePath();
					tfPreprocessingDataInput.setText(file.getAbsolutePath());
				}
			}
		});
		
		tfPreprocessingSaveLocation = new JTextField();
		tfPreprocessingSaveLocation.setEditable(false);
		tfPreprocessingSaveLocation.setColumns(10);
		
		JButton jbPreprocessingSaveLocation = new JButton("Select Output File Location");
		jbPreprocessingSaveLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Choose file
				final JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				
				int returnVal = fileChooser.showSaveDialog(GUI.this);
				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					
					saveLocation = file.getAbsolutePath() + "\\";
					tfPreprocessingSaveLocation.setText(saveLocation);

				}
				
			}
		});
		
		JScrollPane spPreprocessingResults = new JScrollPane();
		GroupLayout gl_jpPreProcessing = new GroupLayout(jpPreProcessing);
		gl_jpPreProcessing.setHorizontalGroup(
			gl_jpPreProcessing.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_jpPreProcessing.createSequentialGroup()
					.addContainerGap()
					.addComponent(spPreprocessingResults, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE)
					.addGap(18)
					.addGroup(gl_jpPreProcessing.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_jpPreProcessing.createParallelGroup(Alignment.LEADING, false)
							.addComponent(jbPreprocessingSaveLocation)
							.addComponent(btnPreProcessingSelectInput)
							.addComponent(tfPreprocessingDataInput, GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE)
							.addComponent(tfPreprocessingSaveLocation))
						.addComponent(btnPreProcessingStart))
					.addContainerGap())
		);
		gl_jpPreProcessing.setVerticalGroup(
			gl_jpPreProcessing.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpPreProcessing.createSequentialGroup()
					.addGap(33)
					.addComponent(tfPreprocessingDataInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(16)
					.addComponent(btnPreProcessingSelectInput)
					.addGap(27)
					.addComponent(tfPreprocessingSaveLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(19)
					.addComponent(jbPreprocessingSaveLocation)
					.addPreferredGap(ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
					.addComponent(btnPreProcessingStart)
					.addGap(58))
				.addGroup(gl_jpPreProcessing.createSequentialGroup()
					.addGap(13)
					.addComponent(spPreprocessingResults, GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		taPreprocessingSummary = new JTextArea();
		spPreprocessingResults.setViewportView(taPreprocessingSummary);
		taPreprocessingSummary.setFont(new Font("Monospaced", Font.PLAIN, 11));
		taPreprocessingSummary.setText("Results Summary");
		jpPreProcessing.setLayout(gl_jpPreProcessing);
		
		JPanel jpProcessing = new JPanel();
		tabbedPane.addTab("Data Mining/Processing", null, jpProcessing, null);
		
		JScrollPane spDataMiningResults = new JScrollPane();
		
		JButton btnDataMiningRuleOutput = new JButton("Select Rule File Output Location");
		
		JButton btntfDataMiningTrainingInput = new JButton("Select Training Set");
		
		tfDataMiningTrainingInput = new JTextField();
		tfDataMiningTrainingInput.setEditable(false);
		tfDataMiningTrainingInput.setColumns(10);
		
		tfDataMiningRuleOutput = new JTextField();
		tfDataMiningRuleOutput.setEditable(false);
		tfDataMiningRuleOutput.setColumns(10);
		
		JButton btnDataMiningStart = new JButton("Start");
		
		JComboBox cbDataMiningRuleSelect = new JComboBox();
		
		JComboBox cbDataMiningWinnerSelect = new JComboBox();
		
		JComboBox cbDataMiningWindowSelect = new JComboBox();
		
		JComboBox cbDataMiningIterationCount = new JComboBox();
		GroupLayout gl_jpProcessing = new GroupLayout(jpProcessing);
		gl_jpProcessing.setHorizontalGroup(
			gl_jpProcessing.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpProcessing.createSequentialGroup()
					.addContainerGap()
					.addComponent(spDataMiningResults, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_jpProcessing.createParallelGroup(Alignment.LEADING)
						.addComponent(tfDataMiningTrainingInput, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfDataMiningRuleOutput, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnDataMiningRuleOutput, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)
						.addComponent(btntfDataMiningTrainingInput)
						.addComponent(btnDataMiningStart, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_jpProcessing.createSequentialGroup()
							.addGroup(gl_jpProcessing.createParallelGroup(Alignment.LEADING)
								.addComponent(cbDataMiningRuleSelect, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbDataMiningWindowSelect, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
							.addGap(31)
							.addGroup(gl_jpProcessing.createParallelGroup(Alignment.LEADING)
								.addComponent(cbDataMiningIterationCount, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbDataMiningWinnerSelect, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_jpProcessing.setVerticalGroup(
			gl_jpProcessing.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_jpProcessing.createSequentialGroup()
					.addContainerGap(13, Short.MAX_VALUE)
					.addGroup(gl_jpProcessing.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_jpProcessing.createSequentialGroup()
							.addGap(3)
							.addComponent(spDataMiningResults, GroupLayout.PREFERRED_SIZE, 391, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_jpProcessing.createSequentialGroup()
							.addGap(23)
							.addComponent(tfDataMiningTrainingInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(16)
							.addComponent(btntfDataMiningTrainingInput)
							.addGap(27)
							.addComponent(tfDataMiningRuleOutput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(19)
							.addComponent(btnDataMiningRuleOutput)
							.addGap(32)
							.addGroup(gl_jpProcessing.createParallelGroup(Alignment.BASELINE)
								.addComponent(cbDataMiningRuleSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbDataMiningWinnerSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_jpProcessing.createParallelGroup(Alignment.BASELINE)
								.addComponent(cbDataMiningWindowSelect, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(cbDataMiningIterationCount, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnDataMiningStart)))
					.addContainerGap())
		);
		
		JTextArea taDataMiningResults = new JTextArea();
		spDataMiningResults.setViewportView(taDataMiningResults);
		jpProcessing.setLayout(gl_jpProcessing);
		
		JPanel jpPostProcessing = new JPanel();
		tabbedPane.addTab("Post-Processing", null, jpPostProcessing, null);
		
		JScrollPane spPostProcessingResults = new JScrollPane();
		
		JButton btnPostProcessingDataFileInput = new JButton("Select Data File");
		
		JButton btnPostProcessingRuleInput = new JButton("Select Rule File");
		
		tfPostProcessingRuleInput = new JTextField();
		tfPostProcessingRuleInput.setEditable(false);
		tfPostProcessingRuleInput.setColumns(10);
		
		tfPostProcessingDataFileInput = new JTextField();
		tfPostProcessingDataFileInput.setEditable(false);
		tfPostProcessingDataFileInput.setColumns(10);
		
		JButton btnPostProcessingStart = new JButton("Start");
		
		JScrollPane spPostProcessingOutputArea = new JScrollPane();
		GroupLayout gl_jpPostProcessing = new GroupLayout(jpPostProcessing);
		gl_jpPostProcessing.setHorizontalGroup(
			gl_jpPostProcessing.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_jpPostProcessing.createSequentialGroup()
					.addContainerGap()
					.addComponent(spPostProcessingResults, GroupLayout.PREFERRED_SIZE, 272, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_jpPostProcessing.createParallelGroup(Alignment.LEADING)
						.addComponent(tfPostProcessingRuleInput, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
						.addComponent(tfPostProcessingDataFileInput, GroupLayout.PREFERRED_SIZE, 360, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnPostProcessingDataFileInput, GroupLayout.PREFERRED_SIZE, 161, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnPostProcessingRuleInput, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnPostProcessingStart, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
						.addComponent(spPostProcessingOutputArea, GroupLayout.PREFERRED_SIZE, 188, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_jpPostProcessing.setVerticalGroup(
			gl_jpPostProcessing.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_jpPostProcessing.createSequentialGroup()
					.addContainerGap(13, Short.MAX_VALUE)
					.addGroup(gl_jpPostProcessing.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_jpPostProcessing.createSequentialGroup()
							.addGap(3)
							.addComponent(spPostProcessingResults, GroupLayout.PREFERRED_SIZE, 391, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_jpPostProcessing.createSequentialGroup()
							.addGap(23)
							.addComponent(tfPostProcessingRuleInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(16)
							.addComponent(btnPostProcessingRuleInput)
							.addGap(27)
							.addComponent(tfPostProcessingDataFileInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(19)
							.addComponent(btnPostProcessingDataFileInput)
							.addGap(40)
							.addComponent(spPostProcessingOutputArea)
							.addGap(18)
							.addComponent(btnPostProcessingStart)))
					.addContainerGap())
		);
		
		JTextArea taPostProcessingOutputArea = new JTextArea();
		spPostProcessingOutputArea.setViewportView(taPostProcessingOutputArea);
		
		JTextArea taPostProcessingResults = new JTextArea();
		spPostProcessingResults.setViewportView(taPostProcessingResults);
		jpPostProcessing.setLayout(gl_jpPostProcessing);
	}
}
