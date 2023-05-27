import { configureStore } from '@reduxjs/toolkit';
import auth from 'src/redux/reducers/authSlice';
import settingsTheme from 'src/redux/reducers/themeSlice';
import user from 'src/redux/reducers/userSlice';

export const store = configureStore({
  reducer: {
    auth,
    settingsTheme,
    user,
  },
});
