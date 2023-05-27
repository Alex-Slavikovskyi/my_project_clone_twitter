import { Typography } from "@mui/material";
import { useSelector } from "react-redux";

export function UserBio() {
  const userBio = useSelector((state) => state.auth.bio);
  return (
    userBio && (
      <Typography
        sx={{
          // color: "rgb(139, 152, 165)",
          padding: "14px 0 0",
        }}
      >
        {userBio}
      </Typography>
    )
  );
}
