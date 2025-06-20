import type { Task } from '../types/Task';
import mockData from '../mocks/tasks.json';

// Assert the mock data to match our Task type
let tasks = [...mockData.tasks] as Task[];

export const TaskService = {
  getAllTasks: async (): Promise<Task[]> => {
    return tasks;
  },

  getTaskById: async (id: string): Promise<Task> => {
    const task = tasks.find(t => t.id === id);
    if (!task) {
      throw new Error('Task not found');
    }
    return task;
  },

  createTask: async (task: Omit<Task, 'id'>): Promise<Task> => {
    const newTask = {
      ...task,
      id: (Math.max(...tasks.map(t => parseInt(t.id))) + 1).toString()
    };
    tasks.push(newTask);
    return newTask;
  },

  updateTask: async (id: string, task: Partial<Task>): Promise<Task> => {
    const index = tasks.findIndex(t => t.id === id);
    if (index === -1) {
      throw new Error('Task not found');
    }
    tasks[index] = { ...tasks[index], ...task };
    return tasks[index];
  },

  deleteTask: async (id: string): Promise<void> => {
    const index = tasks.findIndex(t => t.id === id);
    if (index !== -1) {
      tasks.splice(index, 1);
    }
  }
};
