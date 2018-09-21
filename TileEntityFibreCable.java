package harry.mods.tutorialmod.blocks.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import harry.mods.tutorialmod.handlers.NBTHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityFibreCable extends TileEntity implements ITickable {
	List<TileEntityFibreCable> cables = new ArrayList<>();
	List<TileEntity> inputs = new ArrayList<>(), outputs = new ArrayList<>();

	boolean initialised = false;
	int masterX, masterY, masterZ;
	private boolean isMaster = true;
	boolean hasMaster = false;

	@Override
	public void update() {
		if (!initialised) {
			init();
		}

		if (checkMaster()) {
			if (isMaster) {
				testNeighbors();

				Map<TileEntity, Integer> inputs = new HashMap<>(), outputs = new HashMap<>();
				for (TileEntity te : this.inputs) {
					inputs.put(te, te.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(20, true));
				}

				for (TileEntity te : this.outputs) {
					outputs.put(te, te.getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(20, true));
				}

				Entry[] possibleInputs = inputs.entrySet().stream().filter(entry -> ((int) entry.getValue()) > 0)
						.toArray(Entry[]::new);
				Entry[] possibleOutputs = outputs.entrySet().stream().filter(entry -> ((int) entry.getValue()) > 0)
						.toArray(Entry[]::new);

				for (int i = 0; i < possibleInputs.length; i++) {
					Entry e = possibleInputs[i];
					for (int j = 0; j < possibleOutputs.length; j++) {
						if (e.equals(possibleOutputs[j])) {
							IEnergyStorage storage = ((TileEntity) e.getKey()).getCapability(CapabilityEnergy.ENERGY,
									null);
							if (storage.getEnergyStored() > storage.getMaxEnergyStored() / 2) {
								possibleInputs[i] = null;
							} else {
								possibleOutputs[j] = null;
							}
						}
					}
				}

				int maxIn = 0, maxOut = 0;
				for (Entry entry : possibleInputs) {
					if (entry != null) {
						maxIn += (int) entry.getValue();
					}
				}
				for (Entry entry : possibleOutputs) {
					if (entry != null) {
						maxOut += (int) entry.getValue();
					}
				}

				if (maxIn > maxOut) {
					for (Entry entry : possibleOutputs) {
						if (entry != null)
							((TileEntity) entry.getKey()).getCapability(CapabilityEnergy.ENERGY, null).receiveEnergy(20,
									false);
					}

					int currentIn = 0;
					for (Entry entry : possibleInputs) {
						if (entry != null)
							currentIn += ((TileEntity) entry.getKey()).getCapability(CapabilityEnergy.ENERGY, null)
									.extractEnergy(Math.min(20, maxIn - currentIn), false);
					}
				} else {
					int currentOut = 0;
					for (Entry entry : possibleOutputs) {
						if (entry != null)
							currentOut += ((TileEntity) entry.getKey()).getCapability(CapabilityEnergy.ENERGY, null)
									.receiveEnergy(Math.min(20, maxOut - currentOut), false);
					}

					for (Entry entry : possibleInputs) {
						if (entry != null)
							((TileEntity) entry.getKey()).getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(20,
									false);
					}
				}
			}
		}
	}

	public void onUpdate() {
		if (hasMaster) {
			((TileEntityFibreCable)world.getTileEntity(new BlockPos(masterX, masterY, masterZ))).onUpdate();
		} else {
			init();
		}

		testNeighbors();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		initialised = true;
		hasMaster = compound.getBoolean("hasMaster");
		isMaster = compound.getBoolean("isMaster");

		if (hasMaster) {
			int[] positions = compound.getIntArray("Positions");
			masterX = positions[0];
			masterY = positions[1];
			masterZ = positions[2];
		} else {
			masterX = this.pos.getX();
			masterY = this.pos.getY();
			masterZ = this.pos.getZ();

			BlockPos[] inputPositions = NBTHandler.getPositions((NBTTagCompound) compound.getTag("InputPositions"));
			BlockPos[] outputPositions = NBTHandler.getPositions((NBTTagCompound) compound.getTag("OutputPositions"));

			for (BlockPos pos : inputPositions) {
				TileEntity te = world.getTileEntity(pos);
				inputs.add(te);
			}

			for (BlockPos pos : outputPositions) {
				TileEntity te = world.getTileEntity(pos);
				outputs.add(te);
			}

			int numCables = compound.getInteger("numCables");
			for (int i = 0; i < numCables; i++) {
				NBTTagCompound slave = (NBTTagCompound) compound.getTag("Slave" + i);
				int[] slavePos = slave.getIntArray("Positions");
				TileEntity slaveTE = world.getTileEntity(new BlockPos(slavePos[0], slavePos[1], slavePos[2]));

				if (slaveTE instanceof TileEntityFibreCable) {
					this.cables.add((TileEntityFibreCable) slaveTE);
				}
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setTag("Cable", NBTHandler.toNBT(this));
		compound.setTag("InputPositions", NBTHandler.toNBT(inputs));
		compound.setTag("OutputPositions", NBTHandler.toNBT(outputs));
		return compound;
	}

	private void init() {
		if(isMaster)
		{
			this.masterX = pos.getX();
			this.masterY = pos.getY();
			this.masterZ = pos.getZ();
		}

		initialised = true;
		TileEntity[] surroundings = { world.getTileEntity(pos.up()), world.getTileEntity(pos.down()),
				world.getTileEntity(pos.north()), world.getTileEntity(pos.east()), world.getTileEntity(pos.south()),
				world.getTileEntity(pos.west()) };

		for (TileEntity te : surroundings) {
			if (te != null) {
				if (te instanceof TileEntityFibreCable) {
					TileEntityFibreCable cable_te = (TileEntityFibreCable) te;
					setMasterPos(cable_te.getMasterX(), cable_te.getMasterY(), cable_te.getMasterZ());
				}
			}
		}
	}

	public boolean isInitialised() {
		return initialised;
	}

	public void setInitialised(boolean initialised) {
		this.initialised = initialised;
	}

	public int getMasterX() {
		return masterX;
	}

	public void setMasterX(int masterX) {
		this.masterX = masterX;
	}

	public int getMasterY() {
		return masterY;
	}

	public void setMasterY(int masterY) {
		this.masterY = masterY;
	}

	public int getMasterZ() {
		return masterZ;
	}

	public void setMasterZ(int masterZ) {
		this.masterZ = masterZ;
	}

	public boolean isMaster() {
		return isMaster;
	}

	public void setMaster(boolean isMaster) {
		this.isMaster = isMaster;
	}

	public void setMasterPos(int posX, int posY, int posZ) {
		this.masterX = posX;
		this.masterY = posY;
		this.masterZ = posZ;

		for (TileEntityFibreCable te : cables) {
			if (world.isBlockFullCube(te.getPos())) {
				te.setMasterPos(posX, posY, posZ);
			}
		}

		((TileEntityFibreCable) world.getTileEntity(new BlockPos(posX, posY, posZ))).addSlave(this);
	}

	public void addSlave(TileEntityFibreCable te) {
		cables.add(te);
	}

	private boolean checkMaster() {
		TileEntity master_te = world.getTileEntity(new BlockPos(masterX, masterY, masterZ));

		if (master_te != null) {
			if (master_te instanceof TileEntityFibreCable) {
				TileEntityFibreCable master_tile = (TileEntityFibreCable) master_te;
				if (master_tile.hasMaster) {
					this.setMasterPos(master_tile.getMasterX(), master_tile.getMasterY(), master_tile.getMasterZ());
				}

				return true;
			}
		}

		return false;
	}

	public List<TileEntityFibreCable> getCables() {
		return cables;
	}

	public void setCables(List<TileEntityFibreCable> cables) {
		this.cables = cables;
	}

	public boolean hasMaster() {
		return hasMaster;
	}

	public void setHasMaster(boolean hasMaster) {
		this.hasMaster = hasMaster;
	}

	public void testNeighbors() {
		TileEntity[] surroundings = { world.getTileEntity(pos.up()), world.getTileEntity(pos.down()),
				world.getTileEntity(pos.north()), world.getTileEntity(pos.east()), world.getTileEntity(pos.south()),
				world.getTileEntity(pos.west()) };

		for (TileEntity te : surroundings) {
			if (te != null) {
				if (te.hasCapability(CapabilityEnergy.ENERGY, null)) {
					IEnergyStorage storage = te.getCapability(CapabilityEnergy.ENERGY, null);
					if (storage.canExtract()) {
						inputs.add(te);
					}
					if (storage.canReceive()) {
						outputs.add(te);
					}
				}
			}
		}
	}

	private boolean hasOutput(TileEntity te) {
		return outputs.contains(te);
	}

	private boolean hasInput(TileEntity te) {
		return inputs.contains(te);
	}

	private void setOutput(TileEntity te) {
		if (hasOutput(te) || !this.isMaster) {
			return;
		}

		this.outputs.add(te);
	}

	private void setInput(TileEntity te) {
		if (hasInput(te) || !this.isMaster) {
			return;
		}

		this.inputs.add(te);
	}

}
