/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.app.util.bin.format.pdb2.pdbreader.type;

import java.util.ArrayList;
import java.util.List;

import ghidra.app.util.bin.format.pdb2.pdbreader.*;
import ghidra.app.util.datatype.microsoft.GUID;

/**
 * This class represents the <B>MsType</B> flavor of EOM Definable String 2 type.
 * <P>
 * Note: we do not necessarily understand each of these data type classes.  Refer to the
 *  base class for more information.
 */
public class OemDefinableString2MsType extends AbstractMsType {

	public static final int PDB_ID = 0x1011;

	private GUID guid; // = new GUID(0, (short) 0, (short) 0, initGuidField4);
	private List<RecordNumber> recordNumbers = new ArrayList<>();
	private byte[] remainingBytes;

	/**
	 * Constructor for this type.
	 * @param pdb {@link AbstractPdb} to which this type belongs.
	 * @param reader {@link PdbByteReader} from which this type is deserialized.
	 * @throws PdbException Upon not enough data left to parse.
	 */
	public OemDefinableString2MsType(AbstractPdb pdb, PdbByteReader reader) throws PdbException {
		super(pdb, reader);
		guid = reader.parseGUID();
		int count = reader.parseInt();
		for (int i = 0; i < count; i++) {
			RecordNumber aRecordNumber = RecordNumber.parse(pdb, reader, RecordCategory.TYPE, 32);
			recordNumbers.add(aRecordNumber);
		}
		//TODO: We do not know what "OEM-defined" data remains.  For now, just grabbing rest.
		remainingBytes = reader.parseBytesRemaining();
	}

	@Override
	public int getPdbId() {
		return PDB_ID;
	}

	@Override
	public void emit(StringBuilder builder, Bind bind) {
		builder.append(String.format("OEM Definable String 2\n"));
		builder.append(String.format("  GUID: %s\n", guid.toString()));
		builder.append(String.format("  count: %d\n", recordNumbers.size()));
		for (int i = 0; i < recordNumbers.size(); i++) {
			builder.append(String.format("    recordNumber[%d]: 0x%08x\n", i,
				recordNumbers.get(i).getNumber()));
		}
		builder.append(String.format("  additional data length: %d\n", remainingBytes.length));
	}

}
